import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import auth.AuthData;
import auth.AuthDataReader;
import order.Order;
import order.OrderParser;
import utils.FileViewer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Application {
    private static AuthData authData;

    private static VkApiClient api;
    private static GroupActor groupActor;

    private static SendService sendService;

    public static void main(String[] args) {

        readAuthInfo();

        initApi();

        initServices();

        start();
    }

    private static void readAuthInfo() {
        try {
            authData = AuthDataReader.readAuthData("auth.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void initApi() {
        TransportClient transportClient = new HttpTransportClient();

        api = new VkApiClient(transportClient);

        groupActor = new GroupActor(authData.getGroupId(), authData.getToken());
    }

    private static void initServices() {
        NameConvertorService nameConvertorService = new NameConvertorService(api, groupActor);
        sendService = new SendService(api, groupActor, nameConvertorService);
    }

    private static void start() {
        String[] input;
        Scanner scanner = new Scanner(System.in);

        boolean isInterrupted = false;

        while (!isInterrupted) {
            System.out.print("$ ");

            input = scanner.nextLine().split(" ");

            switch (input[0]) {
                case "exit":
                    isInterrupted = true;
                    System.out.println("Exiting...");
                    break;
                case "help":
                    System.out.println("help - view this text");
                    System.out.println("exit - exit from application");
                    System.out.println("execute fileName - execute .order file");
                    System.out.println("view dirName - view all .order files on directory");
                    break;
                case "view":
                    List<String> fileNames = FileViewer.getAllFilesFromDir(input[1]);
                    System.out.println("Files .order on " + input[1] + "...");
                    if (Optional.ofNullable(fileNames).isPresent())
                        fileNames.forEach(System.out::println);
                    else
                        System.out.println("No any files.");
                    break;
                case "execute":
                    try {
                        Order order = OrderParser.parseOrder(input[1]);
                        sendService.executeSendOrder(order);
                    } catch (IOException e) {
                        System.out.println("File " + input[1] + " is not found");
                        e.printStackTrace();
                        break;
                    }
                    break;
                default:
                    System.out.println("Unknown command. Please, use help to see all commands");
                    break;
            }
        }
    }
}