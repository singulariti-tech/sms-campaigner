package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dao.DatabaseCreator;
import org.springframework.stereotype.Component;

/**
 *
 * @author john.emmanuel
 * @version 0.1
 * @since 0.1
 *
 */
@Component
public class SqliteDatabaseCreator extends BaseSqliteDao implements DatabaseCreator {

    public SqliteDatabaseCreator() {
        System.out.println("Initializing database creator");
    }

    @Override
    public boolean tableExists(String tableName) {
        boolean exists = jdbcTemplate.queryForObject("SELECT name FROM sqlite_master WHERE name='%1'", Boolean.class, tableName);
        return exists;
    }

    @Override
    public void createTables() {
        createIncomingCallTable();
        createIncomingMessageTable();
        createOutgoingMessageTable();
        createAutoReplyRuleTable();
        createContactTable();
        createDndTable();
        createGroupTable();
        createContactGroupJoinTable();
        createIndices();
    }

    @Override
    public void createIndices() {
        System.out.println("Creating indices");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_call_recepit_date] ON [incoming_call] ([recepit_date])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_call_caller_n0] ON [incoming_call] ([caller_no])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_call_process] ON [incoming_call] ([process])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_dnd_registered_date] ON [dnd] ([registered_date])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_message_sender_no] ON [incoming_message] ([sender_no])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_message_encoding] ON [incoming_message] ([encoding])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_message_receipt_date] ON [incoming_message] ([receipt_date])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_incoming_message_message_type] ON [incoming_message] ([message_type])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_recepient_no] ON [outgoing_message] ([recepient_no])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_encoding] ON [outgoing_message] ([encoding])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_status_report] ON [outgoing_message] ([status_report])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_flash_message] ON [outgoing_message] ([flash_message])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_sent_date] ON [outgoing_message] ([sent_date])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_created_date] ON [outgoing_message] ([created_date])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_priority] ON [outgoing_message] ([priority])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_outgoing_message_status] ON [outgoing_message] ([status])");
        jdbcTemplate.update("CREATE INDEX IF NOT EXISTS [idx_contact_name] ON [contact] ([name])");
    }

    public void createIncomingCallTable() {
        System.out.println("Creating incoming call table");
        String sql = "CREATE TABLE IF NOT EXISTS [incoming_call] ("
                + "	[call_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[recepit_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	[gateway_id] VARCHAR(64) NOT NULL ON CONFLICT FAIL, "
                + "	[caller_no] VARCHAR(16) NOT NULL ON CONFLICT FAIL, "
                + "	[process] BOOLEAN NOT NULL DEFAULT 'false'"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createIncomingMessageTable() {
        System.out.println("Creating incoming message table");
        String sql = "CREATE TABLE IF NOT EXISTS [incoming_message] ("
                + "	[incoming_message_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[process] BOOLEAN NOT NULL ON CONFLICT FAIL DEFAULT 'false', "
                + "	[sender_no] VARCHAR(16) NOT NULL ON CONFLICT FAIL, "
                + "	[encoding] CHAR(1) NOT NULL DEFAULT 7, "
                + "	[message_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	[receipt_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	[content] VARCHAR(160) NOT NULL ON CONFLICT FAIL, "
                + "	[message_type] CHAR(1) NOT NULL ON CONFLICT FAIL"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createOutgoingMessageTable() {
        System.out.println("Creating outgoing message table");
        String sql = "CREATE TABLE IF NOT EXISTS [outgoing_message] ("
                + "	[outgoing_message_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[recepient_no] VARCHAR(16) NOT NULL ON CONFLICT FAIL, "
                + "	[sender_no] INTEGER(16) NOT NULL ON CONFLICT FAIL, "
                + "	[content] VARCHAR(640) NOT NULL ON CONFLICT FAIL, "
                + "	[encoding] CHAR(1) NOT NULL ON CONFLICT FAIL DEFAULT 7, "
                + "	[status_report] BOOLEAN NOT NULL ON CONFLICT FAIL DEFAULT 'false', "
                + "	[flash_message] BOOLEAN NOT NULL ON CONFLICT FAIL DEFAULT 'false', "
                + "	[src_port] INT(3) NOT NULL ON CONFLICT FAIL DEFAULT '-1', "
                + "	[dst_port] INT(3) NOT NULL ON CONFLICT FAIL DEFAULT '-1', "
                + "	[created_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	[sent_date] DATETIME, [ref_no] VARCHAR(64), "
                + "	[priority] CHAR(1) NOT NULL ON CONFLICT FAIL DEFAULT 'N', "
                + "	[status] CHAR(1) NOT NULL ON CONFLICT FAIL DEFAULT 'U', "
                + "	[errors] INT(3) NOT NULL ON CONFLICT FAIL DEFAULT 0, "
                + "	[gateway_id] VARCHAR(64), "
                + "	[message_type] CHAR(1) NOT NULL ON CONFLICT FAIL DEFAULT 'B'"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createAutoReplyRuleTable() {
        System.out.println("Creating auto reply rule table");
        String sql = "CREATE TABLE IF NOT EXISTS [auto_reply_rule] ("
                + "	[auto_reply_rule_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[primary_keyword] VARCHAR(45) NOT NULL ON CONFLICT FAIL, "
                + "	[secondary_keyword] VARCHAR(45) NOT NULL ON CONFLICT FAIL, "
                + "	[enabled] BOOLEAN NOT NULL ON CONFLICT FAIL DEFAULT 'true', "
                + "	[content] VARCHAR(160) NOT NULL ON CONFLICT FAIL, "
                + "	[created_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	[modified_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	CONSTRAINT \"UNIQUE_PRIMARY_SECONDARY\" UNIQUE ([secondary_keyword], [primary_keyword]) ON CONFLICT FAIL"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createContactTable() {
        System.out.println("Creating contact table");
        String sql = "CREATE TABLE IF NOT EXISTS [contact] ("
                + "	[contact_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[name] VARCHAR(64) NOT NULL ON CONFLICT FAIL, "
                + "	[mobile_no] VARCHAR(16) NOT NULL ON CONFLICT FAIL, "
                + "	[email] VARCHAR(64) NOT NULL ON CONFLICT FAIL, "
                + "	[address] VARCHAR(512) NOT NULL ON CONFLICT FAIL, "
                + "	CONSTRAINT \"UNIQUE_MOBILE_NO\" UNIQUE ([mobile_no]) ON CONFLICT FAIL"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createDndTable() {
        System.out.println("Creating dnd table");
        String sql = "CREATE TABLE IF NOT EXISTS [dnd] ("
                + "	[dnd_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[registered_date] DATETIME NOT NULL ON CONFLICT FAIL, "
                + "	[mobile_no] VARCHAR(16) NOT NULL ON CONFLICT FAIL, "
                + "	CONSTRAINT \"UNIQUE_MOBILE_NO\" UNIQUE ([mobile_no]) ON CONFLICT FAIL"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createGroupTable() {
        System.out.println("Creating group table");
        String sql = "CREATE TABLE IF NOT EXISTS [group] ("
                + "	[group_id] INTEGER PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT UNIQUE ON CONFLICT FAIL, "
                + "	[name] VARCHAR(128) NOT NULL ON CONFLICT FAIL, "
                + "	CONSTRAINT \"UNIQUE_NAME\" UNIQUE ([name]) ON CONFLICT FAIL"
                + ")";
        jdbcTemplate.update(sql);
    }

    public void createContactGroupJoinTable() {
        System.out.println("Creating contact group join table");
        String sql = "CREATE TABLE IF NOT EXISTS [contact_group_join] ("
                + "	[contact_id] INTEGER REFERENCES [contact](contact_id), "
                + "	[group_id] INTEGER REFERENCES [group](group_id), "
                + "	CONSTRAINT \"UNIQUE_CONTACT_GROUP\" UNIQUE ([contact_id], [group_id]) ON CONFLICT FAIL"
                + ")";
        jdbcTemplate.update(sql);
    }
}
