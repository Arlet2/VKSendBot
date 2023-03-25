package ui.commands;

import ui.UI;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "выводит список всех команд", new String[]{"h"});
    }

    @Override
    public void execute(UI.Context context, String[] args) {
        for (Command i : context.getCommands()) {
            System.out.print(i.name + " - " + i.description);
            if (i.getAliases().length != 0) {
                System.out.print(" (также ");
                for (int j = 0; j < i.getAliases().length; j++) {
                    if (j != 0)
                        System.out.print(", ");
                    System.out.print(i.getAliases()[j]);
                }
                System.out.print(")");
            }
            System.out.print("\n");
        }
    }
}
