package com.payflow.paymentapi.logging;

public class LogMasker {

    public static String maskUserId(String userId) {
        if (userId == null) return "null";
        if (userId.length() <= 2) return "***";

        return userId.charAt(0) + "***" + userId.charAt(userId.length() - 1);
    }

    public static String maskReference(String ref) {
        if (ref == null) return "null";
        if (ref.length() <= 4) return "****";

        return ref.substring(0, 2) + "****";
    }
}
