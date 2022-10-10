package utils;

import auth.AuthData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import java.io.*;
import java.io.FileReader;
import java.util.Optional;

public class DataFileReader {
    private DataFileReader() {

    }

    public static AuthData readAuthJson(String path) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        FileReader reader = new FileReader(path);

        AuthData authData = gson.fromJson(reader, AuthData.class);

        try {
            reader.close();
        } catch (IOException ignored) {
            // программа способна работать и без закрытого файла
        }

        return authData;
    }

    public static Object readEncryptedFile(String path) throws FileNotFoundException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
            Object object = objectInputStream.readObject();

            return object;
        } catch (IOException e) {
            throw new FileReadingException("Cannot open "+path);
        } catch (ClassNotFoundException e) {
            throw new FileReadingException("Incorrect data was saved");
        }
    }

    public static <T> Optional<T> readFile(utils.FileReader<T> reader) {
        T data;
        try {
            data = reader.read();
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
