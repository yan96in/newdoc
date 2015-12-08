package com.bigd.utl;

public class Checking {
    public static boolean isNullorBlank(String s) {
        if (s == null || s.length() <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
