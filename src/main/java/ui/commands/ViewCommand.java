package ui.commands;

import ui.UI;
import utils.FileViewer;

import java.util.List;

public class ViewCommand extends Command {
    private final FileViewer fileViewer = new FileViewer();

    public ViewCommand() {
        super("view", "показать все .order файлы в директории (аргументы dirName)");
    }

    @Override
    public void execute(UI.Context context, String[] args) {
        List<String> fileNames;
        if (args.length == 1) {
            fileNames = fileViewer.getAllFilesFromDir("");
            System.out.println("Файлы .order в этой директории...");
        } else {
            fileNames = fileViewer.getAllFilesFromDir(args[1]);
            System.out.println("Файлы .order в " + args[1] + "...");
        }

        if (fileNames != null)
            fileNames.forEach(System.out::println);
        else
            System.out.println("Нет файлов.");
    }
}
