package pl.kowalecki.dietplannerrestapi.utils;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTools {

    public static String firstToUpper(String text) {
        String value = validateText(text);
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
    public static String allToLower(String text) {
        String value = validateText(text);
        return value.toLowerCase();
    }

    private static String validateText(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text is null or empty");
        }
        else return text;
    }
}
