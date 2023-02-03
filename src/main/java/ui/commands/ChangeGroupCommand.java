package ui.commands;

import ui.UI;

public class ChangeGroupCommand extends Command {
    public ChangeGroupCommand() {
        super("change_group", "поменять группу для отправки (аргументы id и token)",
                new String[]{"cg", "chg"});
    }

    @Override
    public void execute(UI.Context context, String[] args) {
        if (args.length < 3) {
            System.out.println("Недостаточно аргументов. Введите id группы и токен через пробел");
            return;
        }

        try {
            context.getSendService().changeGroupActor(Integer.parseInt(args[1]), args[2]);
            System.out.println("Данные успешно изменены!");
        } catch (NumberFormatException e) {
            System.out.println("ID должно быть целочисленным числом");
        }
    }
}
