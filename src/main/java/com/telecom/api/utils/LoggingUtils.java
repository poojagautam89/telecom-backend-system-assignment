package com.telecom.api.utils;

public final class LoggingUtils {

    private LoggingUtils() {
    }

    /**
     * Mask MSISDN/phone number for logs. Show only last 4 digits.
     * If null/short -> return "****"
     */
    public static String maskMsisdn(String msisdn) {
        if (msisdn == null) return "****";
        String clean = msisdn.trim();
        if (clean.length() <= 4) return "****";
        return "****" + clean.substring(clean.length() - 4);
    }

    /**
     * Mask email by keeping local part initial and domain, e.g. j***@example.com
     * Optional helper if you want to mask email as well.
     */
    public static String maskEmail(String email) {
        if (email == null || email.isBlank()) return "****";
        String[] parts = email.split("@");
        if (parts.length != 2) return "****";
        String local = parts[0];
        String domain = parts[1];
        String visible = !local.isEmpty() ? local.substring(0, 1) : "*";
        return visible + "***@" + domain;
    }
}
