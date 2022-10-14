package utils.files.file_readers;

import utils.exceptions.FileReadingException;

import java.io.*;
import java.util.Optional;

public class DataFileReader {

    public <T> Optional<T> readFile(FileReader<T> reader, String path) {
        T data;
        try {
            data = reader.read(path);
        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
            return Optional.empty();
        } catch (FileReadingException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(data);
    }
}
