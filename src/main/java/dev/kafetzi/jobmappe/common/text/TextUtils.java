package dev.kafetzi.jobmappe.common.text;


public final class TextUtils {
    private TextUtils() {}

    public static String normalize(String value) {
        return value == null ? null : value.strip();
    }
}
