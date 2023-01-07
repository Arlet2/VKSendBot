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

    private final RegexSearcher regexSearcher = new RegexSearcher();
    private final LinkCutter linkCutter = new LinkCutter();

    public Order parseOrder(String path) throws IOException {
        String orderContent = getContentFromFile(path);

        String msg = regexSearcher.searchFirst(MESSAGE_REGEX, orderContent);

        String[] names = regexSearcher.searchFirst(IDS_REGEX, orderContent).split("\n");

        names = Arrays.stream(names)
                .parallel()
                .map(linkCutter::cutLinks)
                .toArray(String[]::new);

        return new Order(msg, names);
    }

    private String getContentFromFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        StringBuilder stringBuilder = new StringBuilder();

        reader.lines().forEach((line) -> {
            stringBuilder.append(line).append("\n");
        });

        return stringBuilder.toString();
    }
}
