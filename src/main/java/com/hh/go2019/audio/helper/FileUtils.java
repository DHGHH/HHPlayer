package com.hh.go2019.audio.helper;

import java.io.File;

public class FileUtils {

    private FileUtils() {
        // do nothing
    }

    public static String formatFileTime(long time) {
        long minute = time / 1000 / 60;
        long second = time / 1000 % 60;
        String min = minute < 10 ? "0" + minute : "" + minute;
        String sec = second < 10 ? "0" + second : "" + second;
        return min + ":" + sec;
    }

    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }
}
