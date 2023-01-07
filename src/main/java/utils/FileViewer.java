package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileViewer {

    public List<String> getAllFilesFromDir(String dirName) {
        File dir = new File(dirName);

        if (!Optional.ofNullable(dir.listFiles()).isPresent())
            return null;

        return Arrays.stream(dir.listFiles())
                .map(File::getName)
                .filter(name -> name.matches(".*\\.order"))
                .collect(Collectors.toList());
    }
}
