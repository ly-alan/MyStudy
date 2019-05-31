package android.app;

/**
 * Created by elegant.wang on 2016/5/12.
 */
public class SystemWriteManager {
    public String getProperty(String key) {
        return null;
    }

    public String getPropertyString(String key, String def) {
        return null;
    }

    public int getPropertyInt(String key, int def) {
        return -1;
    }

    public long getPropertyLong(String key, long def) {
        return -1;
    }

    public boolean getPropertyBoolean(String key, boolean def) {
        return false;
    }

    public boolean setProperty(String key, String value) {
        return false;
    }

    public String readSysfs(String path) {
        return null;
    }

    public boolean writeSysfs(String path, String value) {
        return false;
    }
}
