package com.alteregos.sms.campaigner.business;

/**
 * Date: 23-Oct-2009
 * Time: 11:56:37
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public enum BaudRate {
    BR9600 (9600),
    BR14400 (14400),
    BR19200 (19200),
    BR28800 (28800),
    BR33600 (33600),
    BR38400 (38400),
    BR56000 (56000),
    BR57600 (57600),
    BR115200 (115200);

    int value;

    BaudRate (int value) {
        this.value = value;
    }

    public int getValue () {
        return value;
    }

    public static BaudRate getRate (int value) {
        for (BaudRate rate : values ()) {
            if (rate.getValue () == value) {
                return rate;
            }
        }
        return BaudRate.BR9600;
    }

    @Override
    public String toString () {
        return String.valueOf (value);
    }
}
