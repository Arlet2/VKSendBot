package ui.commands;

import ui.UI;

public class ExitCommand extends Command {
    public ExitCommand(UI.Context context) {
        super("exit", "завершает работу приложения", new String[]{"quit", "q"}, context);
    }

    @Override
    public void execute(String[] args) {
        context.setProgramInterrupted(true);
        System.out.println("Завершение работы...");
    }
}
