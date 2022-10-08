package ui;

import order.Order;
import order.OrderParser;
import services.SendService;
import utils.FileViewer;
import utils.NotFoundByRegexException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI extends UI {
    private boolean isProgramInterrupted;

    public ConsoleUI(SendService sendService) {
        super(sendService);
    }

    @Override
    public void startInteraction() {
        String[] input;
        Scanner scanner = new Scanner(System.in);

        isProgramInterrupted = false;

        System.out.println("Welcome to SendBot v. " +
                Order.class.getPackage().getImplementationVersion() + " (by Arlet)!");
        System.out.println("https://github.com/Arlet2/VKSendBot for guide for this application");
        System.out.println("\nUse help to see all commands");

        while (!isProgramInterrupted) {
            System.out.print("$ ");

            input = scanner.nextLine().split(" ");

            switch (input[0]) {
                case "q":
                case "quit":
                case "exit":
                    exitCommand();
                    break;
                case "help":
                    helpCommand();
                    break;
                case "view":
                    viewCommand(input);
                    break;
                case "run":
                case "r":
                case "exe":
                case "execute":
                    executeCommand(scanner, input);
                    break;
                default:
                    System.out.println("Unknown command. Please, use help to see all commands");
                    break;
            }
        }
    }

    private void exitCommand() {
        isProgramInterrupted = true;
        System.out.println("Exiting...");
    }

    private void helpCommand() {
        System.out.println("help - view this text");
        System.out.println("exit - exit from application (also exit, quit, q)");
        System.out.println("execute fileName - execute .order file (also run, r, exe)");
        System.out.println("view dirName - view all .order files on directory");
    }

    private void viewCommand(String[] input) {
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

    }

    private void executeCommand(Scanner scanner, String[] input) {
        Order order;
        try {
            order = OrderParser.parseOrder(input[1]);
        } catch (IOException e) {
            System.out.println("File " + input[1] + " is not found");
            e.printStackTrace();
            return;
        } catch (NotFoundByRegexException e) {
            System.out.println("The file is incorrect. Please, read documentation and see examples.");
            return;
        }
        System.out.println("ORDER MESSAGE:");
        System.out.println(order.getMsg());

        System.out.println("Are you sure to continue? (y/n)");
        input[0] = scanner.nextLine();

        if (input[0].equals("y") || input[0].equals("yes")) {
            long startTime = System.currentTimeMillis();
            sendService.executeSendOrder(order);
            long endTime = System.currentTimeMillis();
            System.out.println("Executed for " + (endTime - startTime) + " ms");
        } else
            System.out.println("Order is declined");
    }
}
