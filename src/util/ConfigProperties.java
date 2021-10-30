package util;

import java.io.*;
import java.time.LocalDate;
import java.util.Properties;

public class ConfigProperties {

    private static ConfigProperties configProperties;
    private Properties config = new Properties();
    private InputStream configInput = null;

    public ConfigProperties() throws IOException {
        configInput = new FileInputStream(new File("src/config/configuration.properties").getAbsoluteFile());
        config.load(configInput);
        configInput.close();
    }

    public static Properties getProperties() {
        if (configProperties == null) {
            try {
                configProperties = new ConfigProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configProperties.config;
    }

    public static void guardarProperties(String key, String value) {
        try {
            FileOutputStream oFile = new FileOutputStream(new File("src/config/configuration.properties").getAbsoluteFile());
            getProperties().setProperty(key, value);
            getProperties().store(oFile, "Actualizado " + LocalDate.now());
            oFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
