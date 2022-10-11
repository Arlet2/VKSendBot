package utils;

import utils.exceptions.FileReadingException;
import utils.file_readers.FileReader;

import java.io.*;
import java.util.Optional;

public class DataFileReader {
    private DataFileReader() {

    }

    public static <T> Optional<T> readFile(FileReader<T> reader, String path) {
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
