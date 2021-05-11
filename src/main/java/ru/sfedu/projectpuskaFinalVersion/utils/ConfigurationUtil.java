package ru.sfedu.projectpuskaFinalVersion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static ru.sfedu.projectpuskaFinalVersion.Constants.CFG_DEFAULT_VALUE;
import static ru.sfedu.projectpuskaFinalVersion.Constants.CFG_KEY;

public class ConfigurationUtil {

    private static final Properties configuration = new Properties();

    public ConfigurationUtil() {
    }

    private static Properties getConfiguration() throws IOException {
        if (configuration.isEmpty()) {
            loadConfiguration();
        }
        return configuration;
    }

    private static void loadConfiguration() throws IOException {
        File nf = new File(System.getProperty(CFG_KEY, CFG_DEFAULT_VALUE));
        try (InputStream in = new FileInputStream(nf) // DEFAULT_CONFIG_PATH.getClass().getResourceAsStream(DEFAULT_CONFIG_PATH);
        ) {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }
    /**
     * Gets configuration entry value
     * @param key Entry key
     * @return Entry value by key
     * @throws IOException In case of the configuration file read failure
     */
    //следующему методу мы передаем значения строковой константы, по значению этой строковой константы
    //которая явл ключом, мы подгружаем данные, которые у нас будут содержаться в source'e
    public static String getConfigurationEntry(String key) throws IOException{
        return getConfiguration().getProperty(key);
    }
}
