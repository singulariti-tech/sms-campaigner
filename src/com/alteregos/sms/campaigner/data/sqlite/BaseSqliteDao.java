package com.alteregos.sms.campaigner.data.sqlite;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Date: 26-Oct-2009
 * Time: 18:13:24
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class BaseSqliteDao {

    protected SimpleJdbcTemplate jdbcTemplate;

    @Autowired(required=true)
    public void setDataSource (DataSource dataSource) {
        jdbcTemplate = new SimpleJdbcTemplate (dataSource);
    }

    protected int getLastInsertedId () {
        return jdbcTemplate.queryForInt ("SELECT LAST_INSERT_ROWID()");        
    }
}
