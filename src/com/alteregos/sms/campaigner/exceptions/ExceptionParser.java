package com.alteregos.sms.campaigner.exceptions;

/**
 *
 * @author John Emmanuel
 */
public class ExceptionParser {

    private static final String MYSQL_DUPLICATE_ENTRY_CONSTRAINT = "com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry";
    private static final String SQLITE_DUPLICATE_ENTRY_CONSTRAINT = "Transaction is already completed - do not call commit or rollback more than once per transaction";

    public static ITaskResult getError(Exception ex) {
        String message = ex.getMessage();
        if (message.contains(SQLITE_DUPLICATE_ENTRY_CONSTRAINT)) {
            return new UnsuccessfulTaskResult(ResultMessage.DUPLICATE_ENTRY);
        }
        //TODO Log this so we can support it in future
        return new GenericTaskResult(ResultMessage.GENERIC_MESSAGE, false, ex.getMessage());
    }
}
