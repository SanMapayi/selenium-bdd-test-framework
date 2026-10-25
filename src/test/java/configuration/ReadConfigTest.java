package configuration;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ReadConfigTest {
    @Test
    public void jvmBrowserOverrideWinsOverFileDefaults() {
        String previous = System.getProperty("browser");
        try {
            System.setProperty("browser", "Firefox");
            Assert.assertEquals(ReadConfig.getInstance().getBrowser(), "firefox");
        } finally { restore("browser", previous); }
    }

    @Test
    public void headlessCanBeEnabledWithoutEditingTrackedConfiguration() {
        String previous = System.getProperty("headless");
        try {
            System.setProperty("headless", "true");
            Assert.assertTrue(ReadConfig.getInstance().isHeadless());
        } finally { restore("headless", previous); }
    }

    private static void restore(String key, String previous) {
        if (previous == null) System.clearProperty(key);
        else System.setProperty(key, previous);
    }
}