package ui;

import org.reflections.Reflections;
import services.SendService;
import ui.commands.Command;
import ui.commands.Executable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class UI {
    protected Context context;

    protected UI(SendService sendService) {
        context = new Context(sendService);
    }

    public abstract void startInteraction();

    public static class Context {
        private boolean programInterrupted;
        private final SendService sendService;
        private final List<Command> commands = new ArrayList<>();

        public Context(SendService sendService) {
            this.sendService = sendService;

            createCommands();
        }

        private void createCommands() {
            List<Class<?>> classes = getAnnotatedClasses();

            classes.forEach((commandClass) -> {
                if (commandClass == Command.class)
                    return;

                try {
                    commands.add((Command) commandClass.getDeclaredConstructor(this.getClass()).newInstance(this));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e); // todo: ignore
                }
            });
        }

        private List<Class<?>> getAnnotatedClasses() {
            Reflections reflections = new Reflections(this.getClass().getPackage().getName());
            return new ArrayList<>(reflections.getTypesAnnotatedWith(Executable.class));
        }

        public List<Command> getCommands() {
            return commands;
        }

        public void setProgramInterrupted(boolean programInterrupted) {
            this.programInterrupted = programInterrupted;
        }

        public boolean isProgramInterrupted() {
            return programInterrupted;
        }

        public SendService getSendService() {
            return sendService;
        }
    }
}
