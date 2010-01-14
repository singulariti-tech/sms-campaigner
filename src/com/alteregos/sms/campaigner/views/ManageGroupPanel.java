package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.services.ContactService;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.observablecollections.ObservableCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  John Emmanuel
 */
public class ManageGroupPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ManageGroupPanel.class);
    //Services
    private ContactService contactService;
    //Components
    private javax.swing.JButton addContactsButton;
    private javax.swing.JScrollPane contactsScrollPane;
    private javax.swing.JTable contactsTable;
    private javax.swing.JButton deleteContactsButton;
    private javax.swing.JButton deleteGroupsButton;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JComboBox groupsComboBox;
    private java.util.List<Group> smsgroupList;
    //Binding
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    //Helpers
    private DefaultListCellRenderer defaultListCellRenderer;

    /** Creates new form ManageGroupPanel */
    public ManageGroupPanel() {
        initComponents();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task<Boolean, Void> deleteGroupAction() {
        return new RemoveGroupActionTask(Main.getApplication());
    }

    @Action
    public void addContactsAction() {
        if (groupsComboBox.getSelectedIndex() != -1) {
            Group group = (Group) groupsComboBox.getSelectedItem();
            new GroupsManagerContactsDialog(Main.getApplication().getMainFrame(), true, group, this).setVisible(true);
        }
    }

    @Action
    public Task<Boolean, Void> removeContactsAction() {
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

            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Group) {
                    Group group = (Group) value;
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
            Group group = (Group) groupsComboBox.getSelectedItem();
            Collection<Contact> phoneBookCollection = contactService.getContacts(group);
            if (phoneBookCollection != null) {
                Iterator<Contact> phoneBookIterator = phoneBookCollection.iterator();
                DefaultTableModel tableModel = (DefaultTableModel) contactsTable.getModel();
                tableModel.setRowCount(phoneBookCollection.size());
                int row = 0;
                while (phoneBookIterator.hasNext()) {
                    Contact contact = phoneBookIterator.next();
                    contactsTable.setValueAt(contact.getName(), row, 0);
                    contactsTable.setValueAt(contact.getMobileNo(), row, 1);
                    row++;
                }
            }
        }
    }

    private class RemoveContactsActionTask extends org.jdesktop.application.Task<Boolean, Void> {

        RemoveContactsActionTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Boolean doInBackground() {
            boolean deleted = false;
            Group group = (Group) groupsComboBox.getSelectedItem();
            List<Contact> contactsInGroup = contactService.getContacts(group);
            int[] selected = contactsTable.getSelectedRows();
            List<Contact> toRemove = new ArrayList<Contact>(selected.length);
            for (int index = 0; index < selected.length; index++) {
                int listIndex = contactsTable.convertRowIndexToModel(selected[index]);
                Contact phoneBook = contactsInGroup.get(listIndex);
                toRemove.add(phoneBook);
            }

            try {
                contactService.deleteContacts(group, toRemove);
                deleted = contactsInGroup.removeAll(toRemove);
            } catch (Exception rex) {
                LOGGER.error("-- error when removing contacts from group: {}", rex);
                List<Contact> merged = new ArrayList<Contact>(contactsInGroup.size());
                merged.addAll(toRemove);
                merged.addAll(contactsInGroup);
                contactsInGroup.clear();
                contactsInGroup.addAll(merged);
                deleted = false;
            }
            return deleted;  // return your result

        }

        @Override
        protected void succeeded(Boolean result) {
            boolean deleted = result;
            if (deleted) {
                refreshContactsTable();
                contactsTable.clearSelection();
            } else {
            }
        }
    }

    private class RemoveGroupActionTask extends org.jdesktop.application.Task<Boolean, Void> {

        public RemoveGroupActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            boolean deleted = false;
            Group group = (Group) groupsComboBox.getSelectedItem();
            try {
                contactService.deleteGroup(group);
                smsgroupList.remove(group);
                deleted = true;
            } catch (Exception rollBackException) {
                //TODO VERIFY IF ROLLBACK ADDS GROUP BACK CORRECTLY TO COMBOBOX
                LOGGER.error("-- error when removing group: {}", rollBackException);
                smsgroupList.add(group);
                deleted = false;
            }

            return deleted;
        }

        @Override
        protected void succeeded(Boolean arg0) {
            super.succeeded(arg0);
            boolean deleted = arg0;
            if (deleted) {
                //TODO Should an message dialog be shown here
            }
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        contactService = Main.getApplication().getBean("contactService");

        smsgroupList = ObservableCollections.observableList(contactService.getGroups());
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

            private static final long serialVersionUID = 1L;
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };

            @Override
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

            @Override
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
        this.add(groupLabel, "span, split 2");
        this.add(groupsComboBox, "gap, width 150!, wrap");
        this.add(contactsScrollPane, "spanx 3, grow, push, wrap");
        this.add(deleteGroupsButton, "spanx 3, split 3, right, gapafter 10");
        this.add(addContactsButton);
        this.add(deleteContactsButton);

        bindingGroup.bind();
    }
}
