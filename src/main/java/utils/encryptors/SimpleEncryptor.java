package utils.encryptors;

public class SimpleEncryptor implements EncryptorService {

    public static final int ENCRYPTION_PROTOCOL = 1;

    protected SimpleEncryptor() {

    }

    @Override
    public int getEncryptionProtocol() {
        return ENCRYPTION_PROTOCOL;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return crypt(data);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return crypt(data);
    }

    private byte[] crypt(byte[] data) {
        for (int i = 0; i < data.length; i++)
            data[i] = (byte) (data[i] ^ 0xFF);
        return data;
    }
}
