package com.peter.util;

public class DomainUtil {

    private static final char[] charSet;

    static {
        String number = "0123456789";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String special = ".-";
        charSet = (number + upperCase + lowerCase + special).toCharArray();
    }

    public static boolean isValid(String domain) {
        if (StringUtil.isEmpty(domain))
            return false;
        if (!domain.contains("."))
            return false;
        for (int i = 0; i < domain.length(); i ++) {
            boolean in = false;
            for (int j = 0; j < charSet.length; j ++) {
                if (charSet[j] == domain.charAt(i)) {
                    in = true;
                    break;
                }
            }
            if (!in)
                return false;
        }
        return true;
    }
}
