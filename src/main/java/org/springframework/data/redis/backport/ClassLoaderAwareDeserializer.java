package org.springframework.data.redis.backport;


import org.springframework.core.NestedIOException;
import org.springframework.core.serializer.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ClassLoaderAwareDeserializer implements Deserializer<Object> {
    private final ClassLoader classLoader;


    /**
     * Create a {@code DefaultDeserializer} with default {@link ObjectInputStream}
     * configuration, using the "latest user-defined ClassLoader".
     */
    public ClassLoaderAwareDeserializer() {
        this.classLoader = null;
    }

    /**
     * Create a {@code DefaultDeserializer} for using an {@link ObjectInputStream}
     * with the given {@code ClassLoader}.
     * @since 4.2.1
     * @see ConfigurableObjectInputStream#ConfigurableObjectInputStream(InputStream, ClassLoader)
     */
    public ClassLoaderAwareDeserializer(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    /**
     * Read from the supplied {@code InputStream} and deserialize the contents
     * into an object.
     * @see ObjectInputStream#readObject()
     */
    @Override
    @SuppressWarnings("resource")
    public Object deserialize(InputStream inputStream) throws IOException {
        ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
        try {
            return objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            throw new NestedIOException("Failed to deserialize object type", ex);
        }
    }
}
