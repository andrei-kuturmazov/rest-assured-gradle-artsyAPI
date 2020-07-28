package utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {

    protected static FileInputStream fileInputStream;
    protected static Properties properties;

    static {
        try {
            fileInputStream = new FileInputStream("./src/main/resources/login.properties");
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            Log.error(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

