package com.pudding.tangentninety.utils;

import com.orhanobut.logger.Logger;
import com.pudding.tangentninety.BuildConfig;

/**
 * Created by Error on 2017/6/22 0022.
 */
public class LogUtil {

    private static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = BuildConfig.APPLICATION_ID;

    public static void e(String tag,Object o) {
        if(isDebug) {
            Logger.e(tag, o);
        }
    }

    public static void e(Object o) {
        LogUtil.e(TAG,o);
    }

    public static void w(String tag,Object o) {
        if(isDebug) {
            Logger.w(tag, o);
        }
    }

    public static void w(Object o) {
        LogUtil.w(TAG,o);
    }

    public static void d(String msg) {
        if(isDebug) {
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if(isDebug) {
            Logger.i(msg);
        }
    }
}
