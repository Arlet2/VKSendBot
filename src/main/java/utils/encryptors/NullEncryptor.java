package utils.encryptors;

public class NullEncryptor implements EncryptorService {

    protected NullEncryptor() {

    }

    public static final int ENCRYPTION_PROTOCOL = 0;

    @Override
    public int getEncryptionProtocol() {
        return ENCRYPTION_PROTOCOL;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return data;
    }
}
