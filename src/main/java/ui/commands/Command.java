package ui.commands;

import ui.UI;

@Executable
public abstract class Command {
    protected final String name;
    protected final String description;
    protected final String[] aliases;
    protected final UI.Context context;

    protected Command(String name, String description, String[] aliases, UI.Context context) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;

        this.context = context;
    }

    Command(String name, String description, UI.Context context) {
        this(name, description, new String[]{}, context);
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void execute(String[] args);
}
