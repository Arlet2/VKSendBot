package auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AuthDataReader {
    private AuthDataReader() {

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

    public static AuthData readEncryptedFile(String path) throws FileNotFoundException {
        return null;
    }
}
