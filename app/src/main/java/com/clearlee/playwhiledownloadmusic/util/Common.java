package com.clearlee.playwhiledownloadmusic.util;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by ZerdoorPHPDC on 2017/11/28 0028.
 */

public class Common {

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void deleteFile(final String path) {
        try {
            File rootFile = new File(path);
            if (!rootFile.exists()) return;
            if (rootFile.isDirectory())// 如果是文件夹
            {
                File file[] = rootFile.listFiles();
                for (File file2 : file) {
                    deleteFile(file2.getAbsolutePath());
                }
                rootFile.delete();
            } else {
                rootFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkFileExist(String path) {
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getSecDuration2HMSFormatString(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = getDoubleDigitLeftFillZeroString(minute) + ":" + getDoubleDigitLeftFillZeroString(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                if (hour > 0) {
                    timeStr = getDoubleDigitLeftFillZeroString(hour) + ":" + getDoubleDigitLeftFillZeroString(minute) + ":" + getDoubleDigitLeftFillZeroString(second);
                } else {
                    timeStr = getDoubleDigitLeftFillZeroString(minute) + ":" + getDoubleDigitLeftFillZeroString(second);
                }
            }
        }
        return timeStr;
    }

    public static String getDoubleDigitLeftFillZeroString(int i) {
        if (i >= 0 && i < 10)
            return "0" + Integer.toString(i);
        else
            return "" + i;
    }

}
