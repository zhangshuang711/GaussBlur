package com.example.zhsh.gaussblur;

/**
 * ===========================================
 * 作    者：zhsh
 * 版    本：1.0
 * 创建日期：2017/2/16.
 * 描    述：
 * ===========================================
 */

public class AppUtils {

    private static long lastClickTime;

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 防止快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1400) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
