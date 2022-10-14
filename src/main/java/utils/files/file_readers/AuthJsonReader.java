package utils.files.file_readers;

import auth.AuthData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AuthJsonReader{
    public static AuthData read(String path) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        java.io.FileReader reader = new java.io.FileReader(path);

        AuthData authData = gson.fromJson(reader, AuthData.class);

        try {
            reader.close();
        } catch (IOException ignored) {
            // программа способна работать и без закрытого файла
        }

        return authData;
    }
}
