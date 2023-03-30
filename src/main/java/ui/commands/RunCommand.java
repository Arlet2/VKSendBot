package ui.commands;

import order.Order;
import order.OrderParser;
import ui.UI;
import utils.exceptions.NotFoundByRegexException;

import java.io.IOException;
import java.util.Scanner;

public class RunCommand extends Command {
    private final OrderParser orderParser = new OrderParser();

    public RunCommand(UI.Context context) {
        super("run", "исполнить .order файл (path)", new String[]{"r", "execute", "exe"}, context);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Недостаточно аргументов. Введите путь до .order файла");
            return;
        }

        if (context.getSendService().getGroupActor() == null) {
            System.out.println("Данные группы не найдены. Используйте команду change_group для ввода");
            return;
        }

        Order order;
        try {
            order = orderParser.parseOrder(args[1]);
        } catch (IOException e) {
            System.out.println("Файл " + args[1] + " не найден");
            return;
        } catch (NotFoundByRegexException e) {
            System.out.println("Файл составлен некорректно. Пожалуйста, прочитайте документацию и примеры составления.");
            return;
        }
        System.out.println("Id группы для отправки приказа: " + context.getSendService().getGroupActor().getGroupId());
        System.out.println("Сообщение приказа:");
        System.out.println(order.getMsg());

        System.out.println("Вы уверены, что хотите отправить? (y/n)");
        args[0] = new Scanner(System.in).nextLine();

        if (args[0].equals("y") || args[0].equals("yes")) {
            long startTime = System.currentTimeMillis();
            context.getSendService().executeSendOrder(order);
            long endTime = System.currentTimeMillis();
            System.out.println("Исполнено за " + (endTime - startTime) + " мс");
        } else
            System.out.println("Приказ отменён");
    }
}
