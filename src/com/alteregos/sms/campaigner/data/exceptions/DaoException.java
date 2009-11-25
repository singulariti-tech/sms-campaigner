package com.alteregos.sms.campaigner.data.exceptions;

/**
 * Date: 23-Oct-2009
 * Time: 15:43:26
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class DaoException extends RuntimeException {

    public DaoException () {
    }

    public DaoException (String message) {
        super (message);
    }

    public DaoException (String message, Throwable cause) {
        super (message, cause);
    }

    public DaoException (Throwable cause) {
        super (cause);
    }
}
