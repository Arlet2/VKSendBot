import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import auth.AuthData;
import auth.AuthDataReader;
import order.Order;
import order.OrderParser;
import utils.FileViewer;
import utils.NotFoundByRegexException;


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

        System.out.println("Welcome to SendBot v. " +
                Order.class.getPackage().getImplementationVersion() + " (by Arlet)!");
        System.out.println("https://github.com/Arlet2/VKSendBot for guide for this application");
        System.out.println("\nUse help to see all commands");

        while (!isInterrupted) {
            System.out.print("$ ");

            input = scanner.nextLine().split(" ");

            switch (input[0]) {
                case "q":
                case "quit":
                case "exit":
                    isInterrupted = true;
                    System.out.println("Exiting...");
                    break;
                case "help":
                    System.out.println("help - view this text");
                    System.out.println("exit - exit from application (also exit, quit, q)");
                    System.out.println("execute fileName - execute .order file (also run, r, exe)");
                    System.out.println("view dirName - view all .order files on directory");
                    break;
                case "view":
                    List<String> fileNames;
                    if (input.length == 1)
                        fileNames = FileViewer.getAllFilesFromDir("");
                    else
                        fileNames = FileViewer.getAllFilesFromDir(input[1]);

                    if (input.length == 1)
                        System.out.println("Files .order on this directory...");
                    else
                        System.out.println("Files .order on " + input[1] + "...");

                    if (Optional.ofNullable(fileNames).isPresent())
                        fileNames.forEach(System.out::println);
                    else
                        System.out.println("No any files.");

                    break;
                case "run":
                case "r":
                case "exe":
                case "execute":
                    Order order;
                    try {
                        order = OrderParser.parseOrder(input[1]);
                    } catch (IOException e) {
                        System.out.println("File " + input[1] + " is not found");
                        e.printStackTrace();
                        break;
                    } catch (NotFoundByRegexException e) {
                        System.out.println("The file is incorrect. Please, read documentation and see examples.");
                        break;
                    }
                    System.out.println("ORDER MESSAGE:");
                    System.out.println(order.getMsg());

                    System.out.println("Are you sure to continue? (y/n)");
                    input[0] = scanner.nextLine();

                    if (input[0].equals("y") || input[0].equals("yes")) {
                        long startTime = System.currentTimeMillis();
                        sendService.executeSendOrder(order);
                        long endTime = System.currentTimeMillis();
                        System.out.println("Executed for "+ (endTime - startTime) + " ms");
                    }
                    else
                        System.out.println("Order is declined");

                    break;
                default:
                    System.out.println("Unknown command. Please, use help to see all commands");
                    break;
            }
        }
    }
}