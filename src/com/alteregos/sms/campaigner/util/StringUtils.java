/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author John Emmanuel
 */
public class StringUtils {

    public static String stripAllCharsExceptDigitsPlusAndComma(String string) {
        String replacement = "";
        String expression = "[^\\s,\\d,\\+]";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll(replacement);
    }

    public static String stripSpaces(String stringWithSpaces) {
        String delimiter = "";
        return stripSpaces(stringWithSpaces, delimiter);
    }

    public static String stripSpaces(String stringWithSpaces, String delimiter) {
        String space = "\\s";
        Pattern pattern = Pattern.compile(space);
        Matcher matcher = pattern.matcher(stringWithSpaces);
        return matcher.replaceAll(delimiter);
    }

    public static List<String> tokenizeToList(String str, String delimiter) {
        return tokenizeToList(str, delimiter, true, true);
    }

    public static List<String> tokenizeToList(String str, String delimiter, boolean trimTokens, boolean ignoreEmptyTokens) {
        String input = stripAllCharsExceptDigitsPlusAndComma(str);
        StringTokenizer st = new StringTokenizer(input, delimiter);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    public static String[] tokenizeToStringArray(String str, String delimiter) {
        return tokenizeToStringArray(str, delimiter, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiter, boolean trimTokens, boolean ignoreEmptyTokens) {
        List<String> tokens = tokenizeToList(str, delimiter, trimTokens, ignoreEmptyTokens);
        return toStringArray(tokens);
    }

    public static String[] toStringArray(Collection collection) {
        if (collection == null) {
            return null;
        }
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    public static String toDelimitedString(Collection collection, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }

    public static String toJavaVariableNameFormat(String string) {
        string = stripSpaces(string);
        return new StringBuilder(string.substring(0, 1).toLowerCase()).append(string.substring(1)).toString();
    }

    public static String toTitleCase(String s) {
        if (s.equals("")) {
            return "";
        }
        if (s.length() == 1) {
            return s.toUpperCase();
        } else {
            return new StringBuilder(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).toString();
        }
    }
}
