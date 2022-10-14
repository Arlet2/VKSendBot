package utils.files.file_writers;

import utils.Serializer;
import utils.encryptors.EncryptorService;
import utils.encryptors.EncryptorsFactory;
import utils.encryptors.SimpleEncryptor;
import utils.exceptions.FileReadingException;
import utils.files.ByteObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EncryptedFileWriter implements FileWriter {
    private final EncryptorService encryptor = EncryptorsFactory.getEncryptor(SimpleEncryptor.ENCRYPTION_PROTOCOL);

    public void write(Object object, String path) throws FileNotFoundException {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));

            ByteObject byteObject = new ByteObject(
                    encryptor.encrypt(Serializer.convertObjectToBytes(object))
            );

            objectOutputStream.writeObject(byteObject);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new FileReadingException("Cannot write on " + path);
        }
    }
}
