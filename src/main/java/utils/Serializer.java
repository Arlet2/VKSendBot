package utils;

import utils.exceptions.SerializerException;

import java.io.*;

public class Serializer {

    public Object convertBytesToObject(byte[] bytes) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        return convertStreamToObject(byteStream);
    }

    public Object convertStreamToObject(InputStream stream) {
        try {
            ObjectInputStream objStream = new ObjectInputStream(stream);
            return objStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializerException("Cannot convert bytes to object", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SerializerException("Class was not found to convert from bytes", e);
        }
    }

    public byte[] convertObjectToBytes(Object object) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        pushObjectToStream(object, byteStream);
        return byteStream.toByteArray();
    }

    public void pushObjectToStream(Object object, OutputStream outputStream) {
        try {
            ObjectOutputStream objStream = new ObjectOutputStream(outputStream);

            objStream.writeObject(object);
            objStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializerException("Cannot push " + object.getClass() + " to " + outputStream.getClass(), e);
        }
    }
}
