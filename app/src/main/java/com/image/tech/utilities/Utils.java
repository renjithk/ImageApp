package com.image.tech.utilities;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * This class does all the utility jobs such as encoding and decoding objects.
 * It also save an incoming stream onto a targeted destination
 */
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    /**
     * Encodes serialised object to String
     * @param obj serialised class object
     * @return encoded string
     * @throws java.io.IOException if an error occurs while writing to the target stream.
     */
    public static String encodeToString(Object obj) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        String val = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return val;
    }

    /**
     * Decodes String to serialized object.
     * @param settings valid encoded String
     * @return serialized object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object decodeFromString(String settings) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(settings, Base64.DEFAULT);
        ObjectInputStream objectInputStream = null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        objectInputStream = new ObjectInputStream(inputStream);
        return objectInputStream.readObject();
    }
}
