import utils.encryptors.EncryptorsFactory;
import utils.encryptors.EncryptorService;
import utils.encryptors.NullEncryptor;
import utils.encryptors.SimpleEncryptor;
import utils.exceptions.SerializerException;
import org.junit.Assert;
import org.junit.Test;
import utils.Serializer;

public class EncryptorsTest {

    private boolean checkProtocol(int expected, int usageProtocol) {
        return expected == usageProtocol;
    }

    @Test
    public void nullEncryptorEncryptionTest() throws SerializerException {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(NullEncryptor.ENCRYPTION_PROTOCOL);
        String input = "Hello, world!";

        String result = (String)
                Serializer.convertBytesToObject(encryptor.encrypt(Serializer.convertObjectToBytes(input)));

        Assert.assertEquals(input, result);
    }

    @Test
    public void nullEncryptorProtocolChecking() {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(NullEncryptor.ENCRYPTION_PROTOCOL);
        if (!checkProtocol(NullEncryptor.ENCRYPTION_PROTOCOL, encryptor.getEncryptionProtocol()))
            Assert.fail();
    }

    @Test
    public void simpleEncryptorProtocolChecking() {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(SimpleEncryptor.ENCRYPTION_PROTOCOL);
        if (!checkProtocol(SimpleEncryptor.ENCRYPTION_PROTOCOL, encryptor.getEncryptionProtocol()))
            Assert.fail();
    }

    @Test
    public void nullEncryptorDecryptionTest() throws SerializerException {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(NullEncryptor.ENCRYPTION_PROTOCOL);
        String input = "Hello, world!";

        String result = (String)
                Serializer.convertBytesToObject(
                        encryptor.decrypt(encryptor.encrypt(Serializer.convertObjectToBytes(input)))
                );

        Assert.assertEquals(input, result);
    }

    @Test
    public void simpleEncryptorEncryptionTest() throws SerializerException {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(SimpleEncryptor.ENCRYPTION_PROTOCOL);
        String input = "Hello, world!";

        byte[] result = encryptor.encrypt(Serializer.convertObjectToBytes(input));

        Assert.assertNotEquals(Serializer.convertObjectToBytes(input), result);
    }

    @Test
    public void simpleEncryptorDecryptionTest() throws SerializerException {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(SimpleEncryptor.ENCRYPTION_PROTOCOL);
        String input = "Hello, encryptor!";

        String result = (String) Serializer.convertBytesToObject(
                encryptor.decrypt(encryptor.encrypt(Serializer.convertObjectToBytes(input))));

        Assert.assertEquals(input, result);
    }

    @Test
    public void protocolNumberCollisionTest() {
        EncryptorService encryptor = EncryptorsFactory.getEncryptor(-1);

        if (encryptor.getEncryptionProtocol() != 0)
            Assert.fail();
    }
}
