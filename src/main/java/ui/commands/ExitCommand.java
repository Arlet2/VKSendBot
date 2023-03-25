package ui.commands;

import ui.UI;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "завершает работу приложения", new String[]{"quit", "q"});
    }

    @Override
    public void execute(UI.Context context, String[] args) {
        context.setProgramInterrupted(true);
        System.out.println("Завершение работы...");
    }
}
