package utils.files.file_writers;

import java.io.FileNotFoundException;

public interface FileWriter {
    void write(Object object, String path) throws FileNotFoundException;
}
