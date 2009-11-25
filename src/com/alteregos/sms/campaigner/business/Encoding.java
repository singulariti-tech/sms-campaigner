package com.alteregos.sms.campaigner.business;

/**
 * Date: 22-Oct-2009
 * Time: 14:43:17
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public enum Encoding {
    SEVEN_BIT ("7"),
    EIGHT_BIT ("8"),
    UNICODE_UCS2 ("U");

    private String label;

    Encoding (String label) {
        this.label = label;
    }

    public static Encoding getEncoding (String label) {
        for (Encoding e : values ()) {
            if (e.toString ().equals (label)) {
                return e;
            }
        }
        return SEVEN_BIT;
    }

    @Override
    public String toString () {
        return this.label;
    }
}
