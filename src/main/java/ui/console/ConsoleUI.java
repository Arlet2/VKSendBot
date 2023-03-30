package ui.console;

import order.Order;
import services.SendService;
import ui.UI;
import ui.commands.Command;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI extends UI {

    public ConsoleUI(SendService sendService) {
        super(sendService);
    }

    @Override
    public void startInteraction() {
        String[] input;
        Scanner scanner = new Scanner(System.in);

        context.setProgramInterrupted(false);

        System.out.println("SendBot v. " +
                Order.class.getPackage().getImplementationVersion() + " (by Arlet)!");
        System.out.println("https://github.com/Arlet2/VKSendBot для гайда по приложению");
        System.out.println("\nИспользуйте help для поиска по командам");

        while (!context.isProgramInterrupted()) {
            System.out.print("$ ");

            input = scanner.nextLine().split(" ");

            Optional<Command> optionalCommand = searchCommand(input[0]);

            if (!optionalCommand.isPresent())
                System.out.println("Команда не найдена. Используйте help, чтобы посмотреть все команды");
            else
                optionalCommand.get().execute(input);
        }
    }

    private Optional<Command> searchCommand(String name) {
        for (Command command : context.getCommands()) {
            if (command.getName().equals(name)) {
                return Optional.of(command);
            }
            for (String alias : command.getAliases()) {
                if (alias.equals(name)) {
                    return Optional.of(command);
                }
            }
        }
        return Optional.empty();
    }
}
