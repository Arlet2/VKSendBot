package services;

import auth.AuthData;
import utils.files.file_readers.AuthJsonReader;
import utils.files.file_readers.DataFileReader;
import utils.files.file_readers.EncryptedFileReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {


    private final Map<String, AuthData> profiles;
    private final DataFileReader reader = new DataFileReader();
    private final EncryptedFileReader encryptedFileReader = new EncryptedFileReader();

    AuthService() {
        profiles = new HashMap<>();
    }


    private Optional<Map<String, AuthData>> readEncryptedAuth() {
        return reader.readFile(
                (path) -> (Map<String, AuthData>) encryptedFileReader.read(path),
                "profiles.exc");
    }

    private Optional<AuthData> readAuthJson() {
        return reader.readFile((path) -> new AuthJsonReader().read(path), "auth.json");
    }
}
