package com.blazeautomation.connected_ls_sample;

import android.util.Log;

import com.google.gson.Gson;

public class Loggers {

    private final static Gson gson;
    private final static String TAG = "Client";
    public static String TAG_REMOTES = "Remotes: ";
    public static boolean ENABLE_LOGGERS = true;
    private static boolean ENABLE_VERBOSE = true;
    private static boolean ENABLE_DEBUG = false;
    private static boolean ENABLE_INFO = true;
    private static boolean ENABLE_ERRORS = true;

    static {
        gson = new Gson();
    }

    public static void verbose(String msg) {
        try {
            if (ENABLE_LOGGERS && ENABLE_VERBOSE) {
                Log.v(TAG, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void debug(String msg) {
        try {
            if (ENABLE_LOGGERS && ENABLE_DEBUG) {
                Log.d(TAG, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void info(String msg) {
        try {
            if (ENABLE_LOGGERS && ENABLE_INFO) {
                Log.i(TAG, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(String msg) {
        try {
            if (ENABLE_LOGGERS && ENABLE_ERRORS) {
                Log.e(TAG, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(String ra_rules, String msg) {
        try {
            if (ENABLE_LOGGERS && ENABLE_ERRORS) {
                Log.e(TAG + ra_rules, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void error(String ra_rules, Object msg) {
        try {
            if (ENABLE_LOGGERS && ENABLE_ERRORS) {
                Log.e(TAG + ra_rules, gson.toJson(msg));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static boolean isEnableLoggers() {
        return ENABLE_LOGGERS;
    }

    public static void setEnableLoggers(boolean enableLoggers) {
        ENABLE_LOGGERS = enableLoggers;
    }

    public static boolean isEnableVerbose() {
        return ENABLE_VERBOSE;
    }

    public static void setEnableVerbose(boolean enableVerbose) {
        ENABLE_VERBOSE = enableVerbose;
    }

    public static boolean isEnableDebug() {
        return ENABLE_DEBUG;
    }

    public static void setEnableDebug(boolean enableDebug) {
        ENABLE_DEBUG = enableDebug;
    }

    public static boolean isEnableInfo() {
        return ENABLE_INFO;
    }

    public static void setEnableInfo(boolean enableInfo) {
        ENABLE_INFO = enableInfo;
    }

    public static boolean isEnableErrors() {
        return ENABLE_ERRORS;
    }

    public static void setEnableErrors(boolean enableErrors) {
        ENABLE_ERRORS = enableErrors;
    }
}
