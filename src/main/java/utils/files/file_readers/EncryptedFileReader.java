package utils.files.file_readers;

import utils.Serializer;
import utils.encryptors.EncryptorService;
import utils.encryptors.EncryptorsFactory;
import utils.encryptors.SimpleEncryptor;
import utils.exceptions.FileReadingException;
import utils.files.ByteObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EncryptedFileReader {
    private final EncryptorService encryptor = EncryptorsFactory.getEncryptor(SimpleEncryptor.ENCRYPTION_PROTOCOL);
    private final Serializer serializer = new Serializer();

    public Object read(String path) throws FileNotFoundException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(Paths.get(path)));
            ByteObject byteObject = (ByteObject) objectInputStream.readObject();

            return serializer.convertBytesToObject(encryptor.decrypt(byteObject.getBytes()));
        } catch (IOException e) {
            throw new FileReadingException("Cannot open " + path);
        } catch (ClassNotFoundException e) {
            throw new FileReadingException("Incorrect data was saved");
        }
    }
}
