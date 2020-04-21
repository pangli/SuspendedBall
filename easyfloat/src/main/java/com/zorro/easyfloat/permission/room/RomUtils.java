package com.zorro.easyfloat.permission.room;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Package:   com.lzf.easyfloat.permission.room
 * ClassName: RomUtils
 * Created by Zorro on 2020/4/15 19:10.
 * 备注：判断手机ROM
 */
public class RomUtils {
    private static final String TAG = "RomUtils--->";

    /**
     * 获取 emui 版本号
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            if (emuiVersion != null) {
                String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
                return Double.parseDouble(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4;
    }

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Log.e("RomUtils--->", "get miui version code error, version : " + version);
            }
        }
        return -1;
    }


    public static String getSystemProperty(String propName) {
        String line = "";
        BufferedReader input = null;

        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException e) {
            Log.e("RomUtils--->", "Unable to read sysprop " + propName, e);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e("RomUtils--->", "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static boolean checkIsMeizuRom() {
        String systemProperty = getSystemProperty("ro.build.display.id");
        if (TextUtils.isEmpty(systemProperty)) {
            return false;
        } else {
            return systemProperty.contains("flyme") || systemProperty.toLowerCase().contains("flyme");
        }
    }

    public static boolean checkIs360Rom() {
        return Build.MANUFACTURER.contains("QiKU") || Build.MANUFACTURER.contains("360");
    }

    public static boolean checkIsOppoRom() {
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo");
    }

    public static boolean checkIsVivoRom() {
        return Build.MANUFACTURER.contains("VIVO") || Build.MANUFACTURER.contains("vivo");
    }
}
