package org.demo.yuyang.tweetxmldemo.cache;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created on 3/17/16.
 */
public class DataCleanManager {

    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
        deleteFilesByDirectory(context.getFilesDir());
    }

    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    public static void cleanCustomCache(File file) {
        deleteFilesByDirectory(file);
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                if (child.isDirectory()) {
                    deleteFilesByDirectory(child);
                }
                child.delete();
            }
        }
    }
}
