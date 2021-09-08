package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static ConfigProperties configProperties;
    private Properties config = new Properties();
    private InputStream configInput = null;

    public ConfigProperties() throws IOException {
        configInput = new FileInputStream(new File("config/configuration.properties").getAbsoluteFile());
        config.load(configInput);
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
}
