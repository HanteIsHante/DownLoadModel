package com.example.ht.downloadmodel;

import android.os.Environment;

import java.io.File;

public class SDCardMgr {
    public static String SDCARD_DIR = "/sdcard";
    public static final String APP_DIR = "/Netpas";
    public static final String APP_DIR_TEMP = "/Configuration";

    public static boolean getSDCardStatus() {
        boolean result = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return result;
    }


    public static boolean getSDCardStatusAndCreate() {
        boolean b = getSDCardStatus();
        if (b) {
            createDir();
        }
        return b;
    }

    public static String getAppDir() {
        File sdDir = new File(Environment.getExternalStorageDirectory().getPath());
        SDCARD_DIR = sdDir.getAbsolutePath();
        return SDCARD_DIR + APP_DIR;
    }


    private static boolean createDir() {
        createDir(getAppDir());
        return true;
    }

    public static boolean createDir(String path) {
        boolean isDirectoryCreated = false;
        File file = new File(path);
        boolean exists = (file.exists());
        if (!exists) {
            isDirectoryCreated = file.mkdirs();
            return isDirectoryCreated;
        }
        return isDirectoryCreated;
    }
}