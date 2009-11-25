/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author john.emmanuel
 */
public class PhoneNumberUtils {

    public static boolean isMobileNumber(String number) {
        Pattern regex = Pattern.compile("(0|\\+91)9[0-9]{9}");
        Matcher matcher = regex.matcher(number);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
}
