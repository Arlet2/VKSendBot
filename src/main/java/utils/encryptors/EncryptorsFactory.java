package utils.encryptors;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EncryptorsFactory {
    private static final Class<?> DEFAULT_ENCRYPTOR = NullEncryptor.class;

    private EncryptorsFactory() {

    }

    public static EncryptorService getEncryptor(int encryptionProtocol) {
        List<Class<?>> encryptorsClasses = getEncryptorClasses();

        try {
            Stream<Class<?>> stream = createStreamByFilter(encryptorsClasses,
                    getPredicateForFilter(encryptionProtocol));

            if (!isUniqueProtocol(stream)) {
                System.out.println("This protocol number is using for more than one encryptors or does not exist..\n" +
                        "Default encryptor was loaded");
                return getDefaultEncryptor();
            }

            stream = createStreamByFilter(encryptorsClasses, getPredicateForFilter(encryptionProtocol));

            Class<?> encryptor = getEncryptorClassOrDefaultEncryptorClass(stream);
            return (EncryptorService) encryptor.getDeclaredConstructor().newInstance();

        } catch (Throwable t) {
            t.printStackTrace();
            return getDefaultEncryptor();
        }


    }

    private static List<Class<?>> getEncryptorClasses() {
        Reflections reflections = new Reflections(EncryptorsFactory.class.getPackage().getName());
        return new ArrayList<>(reflections.getTypesAnnotatedWith(Encryptor.class));
    }

    private static Stream<Class<?>> createStreamByFilter
            (Collection<Class<?>> encryptorClasses, Predicate<Class<?>> predicate) {
        return encryptorClasses.stream().filter(predicate);
    }

    private static Predicate<Class<?>> getPredicateForFilter(int encryptionProtocol) {
        return (encryptorClass) -> {
            try {
                if (encryptorClass.isInterface())
                    return false;
                return (int) encryptorClass
                        .getMethod("getEncryptionProtocol")
                        .invoke(encryptorClass.getDeclaredConstructor().newInstance())
                        == encryptionProtocol;
            } catch (NoSuchMethodException | IllegalAccessException |
                    InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
                return false;
            }
        };
    }

    private static boolean isUniqueProtocol(Stream<Class<?>> stream) {
        return stream.count() == 1;
    }

    private static Class<?> getEncryptorClassOrDefaultEncryptorClass(Stream<Class<?>> stream) {
        return stream.findFirst().orElse(DEFAULT_ENCRYPTOR);
    }

    private static EncryptorService getDefaultEncryptor() {
        try {
            return (EncryptorService) DEFAULT_ENCRYPTOR.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}
