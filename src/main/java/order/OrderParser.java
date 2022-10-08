package order;

import utils.LinkCutter;
import utils.RegexSearcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class OrderParser {

    private final static String MESSAGE_REGEX = "(?<=message:\\s{0,10}\\n\\\")[\\w\\W]*(?=\\\")";
    private final static String IDS_REGEX = "(?<=names:\\n)[\\w\\W]*";

    private OrderParser() {

    }

    public static Order parseOrder(String path) throws IOException {
        String orderContent = getContentFromFile(path);

        String msg = RegexSearcher.searchFirst(MESSAGE_REGEX, orderContent);

        String[] names = RegexSearcher.searchFirst(IDS_REGEX, orderContent).split("\n");

        names = Arrays.stream(names)
                .parallel()
                .map(LinkCutter::cutLinks)
                .toArray(String[]::new);

        return new Order(msg, names);
    }

    private static String getContentFromFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        StringBuilder stringBuilder = new StringBuilder();

        reader.lines().forEach((line) -> {
            stringBuilder.append(line).append("\n");
        });

        return stringBuilder.toString();
    }
}
