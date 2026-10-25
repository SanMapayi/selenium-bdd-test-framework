package configuration;

import core.Constants;
import org.slf4j.Logger;
import utilities.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {
    private static volatile ReadConfig instance;
    private final Properties properties;

    private static final Logger logger = LoggerUtil.getLogger();

    // Private constructor (Singleton)
    private ReadConfig() {
        properties = new Properties();
        loadConfig();
    }

    // Singleton instance getter
    public static ReadConfig getInstance() {
        if (instance == null) {
            synchronized (ReadConfig.class) {
                if (instance == null) {
                    instance = new ReadConfig();
                }
            }
        }
        return instance;
    }

    // Load config.properties file
    private void loadConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("No configuration file provided: config/config.properties not found in classpath");
            }
            properties.load(inputStream);
            logger.info("Configuration file loaded successfully from classpath.");
        } catch (IOException e) {
            logger.error("Failed to load file in class %s and thrown an exception (%s)"
                    .formatted(ReadConfig.class.getName(), e));
            throw new RuntimeException("Failed to load config file: " + e.getMessage());
        }
    }

    // Explicit JVM settings override environment variables, then the checked-in defaults.
    private String getProperty(String key, String fallback) {
        String override = System.getProperty(key);
        if (override != null) return override;
        String envKey = key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(java.util.Locale.ROOT);
        String environment = System.getenv(envKey);
        return environment != null ? environment : properties.getProperty(key, fallback);
    }

    // Getter methods for each property
    public String getBrowser() {
        return getProperty("browser", "chrome").toLowerCase(java.util.Locale.ROOT);
    }

    public String getUrl() {
        return getProperty("url", Constants.URL);
    }

    public int getImplicitWait() {
        return Integer.parseInt(getProperty("implicitWait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(getProperty("explicitWait", "15"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("pageLoadTimeout", "20"));
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public String getWindowSize() {
        return getProperty("windowSize", "maximize");
    }
}
