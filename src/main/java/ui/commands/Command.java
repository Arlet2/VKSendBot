package ui.commands;

import ui.UI;

@Executable
public abstract class Command {
    protected final String name;
    protected final String description;
    protected final String[] aliases;

    protected Command(String name, String description, String[] aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    Command(String name, String description) {
        this(name, description, new String[]{});
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

    public abstract void execute(UI.Context context, String[] args);
}
