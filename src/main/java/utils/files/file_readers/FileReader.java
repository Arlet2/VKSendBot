package utils.file_readers;

import java.io.FileNotFoundException;

public interface FileReader<T> {
    T read(String path) throws FileNotFoundException;
}
