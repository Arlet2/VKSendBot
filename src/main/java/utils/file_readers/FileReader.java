package utils;

import java.io.FileNotFoundException;

public interface FileReader<T> {
    T read() throws FileNotFoundException;
}
