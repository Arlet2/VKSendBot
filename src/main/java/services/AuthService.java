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

    AuthService() {
        profiles = new HashMap<>();
    }


    private Optional<Map<String, AuthData>> readEncryptedAuth() {
        return reader.readFile(
                (path) -> (Map<String, AuthData>) EncryptedFileReader.read(path),
                "profiles.exc");
    }

    private Optional<AuthData> readAuthJson() {
        return reader.readFile(AuthJsonReader::read, "auth.json");
    }
}
