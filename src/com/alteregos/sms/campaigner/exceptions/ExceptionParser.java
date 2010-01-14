package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public class ExceptionParser {

    //TODO This is not valid for Sqlite
    private static final String DUPLICATE_ENTRY_CONSTRAINT = "com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry";

    public static ITaskResult getError(Exception ex) {
        String message = ex.getMessage();
        if (message.contains(DUPLICATE_ENTRY_CONSTRAINT)) {
            return new UnsuccessfulTaskResult(ResultMessage.DUPLICATE_ENTRY);
        }
        //TODO Log this so we can support it in future
        return new GenericTaskResult(ResultMessage.GENERIC_MESSAGE, false, ex.getMessage());
    }
}
