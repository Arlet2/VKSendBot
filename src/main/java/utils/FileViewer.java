package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileViewer {

    public Optional<List<String>> getAllFilesFromDir(String dirName) {
        var dir = new File(dirName);

        if (dir.listFiles() == null)
            return Optional.empty();

        return Optional.of(Arrays.stream(dir.listFiles())
                .map(File::getName)
                .filter(name -> name.matches(".*\\.order"))
                .collect(Collectors.toList()));
    }
}
