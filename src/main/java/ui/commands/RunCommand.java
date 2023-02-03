package ui.commands;

import order.Order;
import order.OrderParser;
import ui.UI;
import utils.exceptions.NotFoundByRegexException;

import java.io.IOException;
import java.util.Scanner;

public class RunCommand extends Command {
    private final OrderParser orderParser = new OrderParser();

    public RunCommand() {
        super("run", "исполнить .order файл", new String[]{"r", "execute", "exe"});
    }

    @Override
    public void execute(UI.Context context, String[] args) {
        Order order;
        try {
            order = orderParser.parseOrder(args[1]);
        } catch (IOException e) {
            System.out.println("Файл " + args[1] + " не найден");
            e.printStackTrace();
            return;
        } catch (NotFoundByRegexException e) {
            System.out.println("Файл составлен некорректно. Пожалуйста, прочитайте документацию и примеры составления.");
            return;
        }
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
