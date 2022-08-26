import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class AuthDataReader {
    private AuthDataReader() {

    }

    public static AuthData readAuthData(String path) throws FileNotFoundException {
        FileReader reader = new FileReader(path);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.fromJson(reader, AuthData.class);
    }
}
