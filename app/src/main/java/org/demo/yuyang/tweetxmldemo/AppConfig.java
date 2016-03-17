package org.demo.yuyang.tweetxmldemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created on 3/14/16.
 */
public class AppConfig {
    private final static String APP_CONFIG = "config";
    public final static String CONF_COOKIE = "cookie";

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";

    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    public static final String KEY_NOTIFICATION_ACCEPT = "KEY_NOTIFICATION_ACCEPT";
    public static final String KEY_NOTIFICATION_SOUND = "KEY_NOTIFICATION_SOUND";
    public static final String KEY_NOTIFICATION_VIBRATION = "KEY_NOTIFICATION_VIBRATION";
    public static final String KEY_NOTIFICATION_DISABLE_WHEN_EXIT = "KEY_NOTIFICATION_DISABLE_WHEN_EXIT";
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";
    public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    public static final String KEY_TWEET_DRAFT = "KEY_TWEET_DRAFT";
    public static final String KEY_NOTE_DRAFT = "KEY_NOTE_DRAFT";

    public static final String KEY_FRITST_START = "KEY_FRIST_START";

    public static final String KEY_NIGHT_MODE_SWITCH = "night_mode_switch";

    public static final String APP_QQ_KEY = "100942993";

    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory() + File.separator +
            "OSChina" + File.separator + "osc_img" + File.separator;

    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory() + File.separator +
            "OSChina" + File.separator + "download" + File.separator;

    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

//    public static SharedPreferences getSharedPreferences(Context context) {
//        return P
//    }

    public String get(String key) {
        Properties pros = get();
        return (pros != null) ? pros.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);

        try {
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
        File conf = new File(dirConf, APP_CONFIG);

        try {
            fos = new FileOutputStream(conf);

            /**
             * public void store(OutputStream out, String comment)
             *
             * Stores properties to the specified OutputStream,
             * using ISO-8859-1.
             *
             * @params comment an optional comment to be written, or null.
             */
            p.store(fos, null);

            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);

        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key) {
            props.remove(k);
        }

        setProps(props);
    }
}
