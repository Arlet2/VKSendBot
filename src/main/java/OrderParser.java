import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OrderParser {

    private final static String MESSAGE_REGEX = "(?<=message: \\\")[\\w\\W]*(?=\\\")";
    private final static String IDS_REGEX = "(?<=names:\\n)[\\w\\W]*";

    private OrderParser() {

    }

    public static Order parseOrder(String path) throws FileNotFoundException {
        String orderContent = getContentFromFile(path);

        String msg = RegexSearcher.searchFirst(MESSAGE_REGEX, orderContent);

        String names = RegexSearcher.searchFirst(IDS_REGEX, orderContent);

        return new Order(msg, names.split("\n"));
    }

    private static String getContentFromFile(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        StringBuilder stringBuilder = new StringBuilder();

        reader.lines().forEach((line) -> {
            stringBuilder.append(line).append("\n");
        });

        return stringBuilder.toString();
    }
}
