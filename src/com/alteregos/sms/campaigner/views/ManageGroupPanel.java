package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.beans.Phonebook;
import com.alteregos.sms.campaigner.data.beans.Smsgroup;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.persistence.RollbackException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

/**
 *
 * @author  John Emmanuel
 */
public class ManageGroupPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form ManageGroupPanel */
    public ManageGroupPanel() {
        initComponents();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task deleteGroupAction() {
        return new RemoveGroupActionTask(Main.getApplication());
    }

    @Action
    public void addContactsAction() {
        if (groupsComboBox.getSelectedIndex() != -1) {
            Smsgroup group = (Smsgroup) groupsComboBox.getSelectedItem();
            new GroupsManagerContactsDialog(Main.getApplication().getMainFrame(), true, group, this).setVisible(true);
        }
    }

    @Action
    public Task removeContactsAction() {
        return new RemoveContactsActionTask(Main.getApplication());
    }

    @Action
    public void refreshContactsAction() {
        refreshContactsTable();
    }
    //</editor-fold>

    /**
     *
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void initialize() {
        defaultListCellRenderer = new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Smsgroup) {
                    Smsgroup group = (Smsgroup) value;
                    setText(group.getName());
                }
                return this;
            }
        };
        groupsComboBox.setRenderer(defaultListCellRenderer);
    }

    private void groupsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        refreshContactsTable();
    }

    private void refreshContactsTable() {
        if (groupsComboBox.getSelectedIndex() != -1) {
            Smsgroup group = (Smsgroup) groupsComboBox.getSelectedItem();
            Collection<Phonebook> phoneBookCollection = group.getPhoneBookCollection();
            Iterator<Phonebook> phoneBookIterator = phoneBookCollection.iterator();
            DefaultTableModel tableModel = (DefaultTableModel) contactsTable.getModel();
            tableModel.setRowCount(phoneBookCollection.size());
            int row = 0;
            while (phoneBookIterator.hasNext()) {
                Phonebook contact = phoneBookIterator.next();
                contactsTable.setValueAt(contact.getName(), row, 0);
                contactsTable.setValueAt(contact.getMobileNo(), row, 1);
                row++;
            }
        }
    }

    private class RemoveContactsActionTask extends org.jdesktop.application.Task<Object, Void> {

        RemoveContactsActionTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            boolean deleted = false;
            Smsgroup group = (Smsgroup) groupsComboBox.getSelectedItem();
            List<Phonebook> contactsInGroup = (List) group.getPhoneBookCollection();
            int[] selected = contactsTable.getSelectedRows();
            List<com.alteregos.sms.campaigner.data.beans.Phonebook> toRemove = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(selected.length);
            for (int index = 0; index < selected.length; index++) {
                int listIndex = contactsTable.convertRowIndexToModel(selected[index]);
                com.alteregos.sms.campaigner.data.beans.Phonebook phoneBook = contactsInGroup.get(listIndex);
                toRemove.add(phoneBook);
            }

            try {
                entityManager.getTransaction().begin();
                deleted = contactsInGroup.removeAll(toRemove);
                entityManager.merge(group);
                entityManager.getTransaction().commit();
            } catch (RollbackException rex) {
                log.error("Error when removing contacts from group");
                log.error(rex);
                List<com.alteregos.sms.campaigner.data.beans.Phonebook> merged = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(contactsInGroup.size());
                merged.addAll(toRemove);
                merged.addAll(contactsInGroup);
                contactsInGroup.clear();
                contactsInGroup.addAll(merged);
                deleted = false;
            }
            return deleted;  // return your result

        }

        @Override
        protected void succeeded(Object result) {
            boolean deleted = (Boolean) result;
            System.out.println("Deleted: " + deleted);
            if (deleted) {
                refreshContactsTable();
                contactsTable.clearSelection();
            } else {
            }
        }
    }

    private class RemoveGroupActionTask extends org.jdesktop.application.Task<Object, Void> {

        public RemoveGroupActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
            boolean deleted = false;
            Smsgroup group = (Smsgroup) groupsComboBox.getSelectedItem();
            try {
                entityManager.getTransaction().begin();
                smsgroupList.remove(group);
                entityManager.remove(group);
                entityManager.getTransaction().commit();
                deleted = true;
            } catch (RollbackException rollBackException) {
                //TODO VERIFY IF ROLLBACK ADDS GROUP BACK CORRECTLY TO COMBOBOX
                log.error(rollBackException);
                smsgroupList.add(group);
                deleted = false;
            }

            return deleted;
        }

        @Override
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
            boolean deleted = (Boolean) arg0;
            if (deleted) {
                //TODO Should an message dialog be shown here
            }
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        smsgroupQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT s FROM Smsgroup s");
        smsgroupList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(smsgroupQuery.getResultList());
        groupLabel = new javax.swing.JLabel();
        contactsScrollPane = new javax.swing.JScrollPane();
        contactsTable = new javax.swing.JTable();
        groupsComboBox = new javax.swing.JComboBox();
        deleteContactsButton = new javax.swing.JButton();
        addContactsButton = new javax.swing.JButton();
        deleteGroupsButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(ManageGroupPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N
        this.setName("borderContainer"); // NOI18N

        groupLabel.setText(resourceMap.getString("groupLabel.text")); // NOI18N
        groupLabel.setName("groupLabel"); // NOI18N

        contactsScrollPane.setName("contactsScrollPane"); // NOI18N

        contactsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null},
                    {null, null},
                    {null, null},
                    {null, null}
                },
                new String[]{
                    "Contact", "Mobile No."
                }) {

            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        contactsTable.setName("contactsTable"); // NOI18N
        contactsScrollPane.setViewportView(contactsTable);
        contactsTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("contactsTable.columnModel.title0")); // NOI18N
        contactsTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("contactsTable.columnModel.title1")); // NOI18N

        groupsComboBox.setName("groupsComboBox"); // NOI18N

        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, smsgroupList, groupsComboBox);
        bindingGroup.addBinding(jComboBoxBinding);

        groupsComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(ManageGroupPanel.class, this);
        deleteContactsButton.setAction(actionMap.get("removeContactsAction")); // NOI18N
        deleteContactsButton.setName("deleteContactsButton"); // NOI18N

        addContactsButton.setAction(actionMap.get("addContactsAction")); // NOI18N
        addContactsButton.setName("addContactsButton"); // NOI18N

        deleteGroupsButton.setAction(actionMap.get("deleteGroupAction")); // NOI18N
        deleteGroupsButton.setName("deleteGroupsButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets panel", "[][grow][]", "[][grow][]"));
        this.add(groupLabel);
        this.add(groupsComboBox, "grow, push, wrap");
        this.add(contactsScrollPane, "spanx 3, grow, push, wrap");
        this.add(deleteGroupsButton, "spanx 3, split 3, right, gapafter 10");
        this.add(addContactsButton);
        this.add(deleteContactsButton);

        bindingGroup.bind();
    }
    private javax.swing.JButton addContactsButton;
    private javax.swing.JScrollPane contactsScrollPane;
    private javax.swing.JTable contactsTable;
    private javax.swing.JButton deleteContactsButton;
    private javax.swing.JButton deleteGroupsButton;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JComboBox groupsComboBox;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Smsgroup> smsgroupList;
    private javax.persistence.Query smsgroupQuery;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private static Logger log = LoggerHelper.getLogger();
    private DefaultListCellRenderer defaultListCellRenderer;
}
