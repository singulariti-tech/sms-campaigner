package com.alteregos.sms.campaigner.data.sqlite;

import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.exceptions.DaoException;
import com.alteregos.sms.campaigner.data.mappers.ContactRowMapper;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Date: 23-Oct-2009
 * Time: 16:20:19
 *
 * @author John Emmanuel
 * @version 0.1
 * @since 0.1
 */
public class SqliteContactDao extends BaseSqliteDao implements com.alteregos.sms.campaigner.data.dao.ContactDao {

    private final String TABLE_NAME = "contact";
    private final String DEFAULT_SELECTORS = " contact_id, address, email, mobile_no, name ";
    private String findByIdQuery;
    private String findAllQuery;
    private String findByMobileNoQuery;
    private String insertStmt;
    private String updateStmt;
    private String deleteStmt;

    public SqliteContactDao() {
        findByIdQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE contact_id = ?";
        findAllQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " ORDER BY contact_id ASC";
        findByMobileNoQuery = "SELECT" + DEFAULT_SELECTORS + "FROM " + TABLE_NAME + " WHERE mobile_no = ? ORDER BY contact_id ASC";
        insertStmt = "INSERT INTO " + TABLE_NAME + " (" + DEFAULT_SELECTORS + ") VALUES(?, ?, ?, ?, ?)";
        updateStmt = "UPDATE " + TABLE_NAME + " SET address = ?, email = ?, mobile_no = ?, name = ? WHERE contact_id = ?";
        deleteStmt = "DELETE " + TABLE_NAME + " WHERE contact_id = ?";
    }

    @Override
    public Contact findById(int contactId) {
        Contact contact;
        try {
            contact = jdbcTemplate.queryForObject(findByIdQuery, new ContactRowMapper(), contactId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return contact;
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts;
        try {
            contacts = jdbcTemplate.query(findAllQuery, new ContactRowMapper());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return contacts;
    }

    @Override
    public List<Contact> findByMobileNo(String mobileNo) {
        List<Contact> contacts;
        try {
            contacts = jdbcTemplate.query(findByMobileNoQuery, new ContactRowMapper(), mobileNo);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return contacts;
    }

    @Override
    public synchronized int insert(Contact contact) {
        int lastId;
        try {
            jdbcTemplate.update(insertStmt, null, contact.getAddress(), contact.getEmail(), contact.getMobileNo(),
                    contact.getName());
            lastId = getLastInsertedId();
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
        return lastId;
    }

    @Override
    public synchronized void update(Contact contact) {
        try {
            jdbcTemplate.update(updateStmt, contact.getAddress(), contact.getEmail(), contact.getMobileNo(),
                    contact.getName(), contact.getContactId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public synchronized void update(List<Contact> contacts) {
        try {
            int[] counts = null;
            List<Object[]> batch = new ArrayList<Object[]>();
            for (Contact contact : contacts) {
                Object[] oa = new Object[]{
                    contact.getAddress(), contact.getEmail(), contact.getMobileNo(),
                    contact.getName(), contact.getContactId()
                };
                batch.add(oa);
            }
            counts = jdbcTemplate.batchUpdate(updateStmt, batch);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public synchronized void delete(Contact contact) {
        try {
            jdbcTemplate.update(deleteStmt, contact.getContactId());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public synchronized void delete(List<Contact> contacts) {
        try {
            StringBuffer deleteBulkStmt = new StringBuffer("DELETE FROM " + TABLE_NAME + " WHERE contact_id in (");
            int size = contacts.size();
            int lessOne = size - 1;
            for (int i = 0; i < size; i++) {
                Contact r = contacts.get(i);
                deleteBulkStmt.append(r.getContactId());
                if (i < lessOne) {
                    deleteBulkStmt.append(", ");
                }
            }
            deleteBulkStmt.append(")");
            jdbcTemplate.update(deleteBulkStmt.toString());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
