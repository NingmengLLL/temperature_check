package com.jhlkdz.temperatremeasure.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {

    public static boolean isEmpty(String string) {
        return (string == null || string.length() == 0);
    }

    public static boolean isNum(String str) {
        String regEx = "^[0-9]+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            return true;
        }
        else {
            return false;
        }
    }

}
