package dev.abykov.pets.relmgr;

import java.io.InputStream;
import java.util.Properties;

public class AppInfo {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = AppInfo.class.getResourceAsStream("/app.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception ignored) {
        }
    }

    public static String version() {
        return props.getProperty("version", "dev");
    }
}
