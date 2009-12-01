package com.alteregos.sms.campaigner.data.dao;

/**
 *
 * @author john.emmanuel
 * @version
 * @since
 */
public interface DatabaseCreator {
    boolean tableExists(String tableName);
    boolean allTablesExist();
    void createTables();
    void createIndices();
}
