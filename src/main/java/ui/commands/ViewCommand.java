package ui.commands;

import ui.UI;
import utils.FileViewer;

import java.util.List;
import java.util.Optional;

public class ViewCommand extends Command {
    private final FileViewer fileViewer = new FileViewer();

    public ViewCommand(UI.Context context) {
        super("view", "показать все .order файлы в директории (аргументы dirName)", context);
    }

    @Override
    public void execute(String[] args) {
        Optional<List<String>> fileNames;
        if (args.length == 1) {
            fileNames = fileViewer.getAllFilesFromDir("");
            System.out.println("Файлы .order в этой директории...");
        } else {
            fileNames = fileViewer.getAllFilesFromDir(args[1]);
            System.out.println("Файлы .order в " + args[1] + "...");
        }

        if (fileNames.isPresent())
            fileNames.get().forEach(System.out::println);
        else
            System.out.println("Нет файлов.");
    }
}
