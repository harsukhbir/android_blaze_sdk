package com.blazeautomation.connected_ls_sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

final class BlazeUtils {
    public static final String HUB_SSID_WITH_BRACKET = "Home-Ultimate(";
    private static Pattern pattern2 = Pattern.compile("[a-zA-Z]");

    public static String getTimeZoneDifferenceTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z", Locale.ENGLISH).format(calendar.getTime());
        return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

    public static long getTimeInMills() {
        return System.currentTimeMillis() / 1000;
    }

    public static boolean isStringNotEmpty(String str) {
        return null != str && !str.isEmpty();
    }

    public static boolean compare(String beforeDelaySecurityStatus, String s) {
        if (s == null) return false;
        return s.equalsIgnoreCase(beforeDelaySecurityStatus);
    }


    @SuppressLint({"HardwareIds", "MissingPermission"})
    //@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    static String getId(Context context) {
        try {
            String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (id != null)
                return id;
            return UUID.randomUUID().toString();

            /*TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null) {
                if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
                    //return "Tablet";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        return getSerial();
                    }
                    Loggers.d("UTILS","TABLET "+ Build.SERIAL);
                    return Build.SERIAL;
                } else {
                    return manager.getDeviceId();
                    //return "Mobile";
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static boolean isValidEmail(String email) {
        /*if (Character.isDigit(email.charAt(0))) {
            // Loggers.error("first character is number so it is not valid");
            return false;
        }*/
        if (!Character.isAlphabetic(email.charAt(0)) && !Character.isDigit(email.charAt(0))) {
            //  Loggers.error("first character should be alphabetic so it is not valid");
            return false;
        }
        //(),:;<>@[\]
        if (email.contains(" ") || email.contains("(") || email.contains(")") || email.contains("'")
                || email.contains("\"") || email.contains(",") || email.contains("\\") ||
                email.contains("/") || email.contains("[") || email.contains("]")
                || email.contains(":") || email.contains(";") || email.contains("<") || email.contains(">")) {
            // Loggers.error("Spaces or few special chars are not allowed");
            return false;
        }

        boolean at = email.contains("@");
        if (!at) return false;

        boolean dot = email.contains(".");
        if (!dot) {
            //Loggers.error("there is no dot");
            return false;
        }

        int countAt = 0;
        for (int i = 0; i < email.length(); i++)
            if (email.charAt(i) == '@') {
                countAt++;
            }
        if (countAt > 1 || countAt == 0) {
            // Loggers.error("@ occured more than 1 time");
            return false;
        }
        String[] parts = email.split("@");

        if (!parts[1].contains(".") || parts[1].endsWith(".")) {
            return false;
        }
        for (String p : parts) {
            if (p.length() == 0) {
                //Loggers.error("after @ there is no character");
                return false;
            }
            if (/*Character.isDigit(p.charAt(0)) ||*/ p.charAt(0) == '.' || p.charAt(p.length() - 1) == '.') {
//            if (Character.isDigit(p.charAt(0)) || p.charAt(0) == '.' || p.charAt(p.length() - 1) == '.') {
                // Loggers.error("first character is number or dot is at last so " + p + " is not valid");
                return false;
            }
        }
        String[] dotpart = email.split(".");
        for (String s : dotpart) {
            if (s.length() == 0) {
                return false;
            }
        }
        return true;

    }

    static boolean isValidPasssword(String pwd) {
        return pwd != null && pwd.length() >= 6 && (pwd.matches(".*\\d+.*") && pattern2.matcher(pwd).find());
    }

}
