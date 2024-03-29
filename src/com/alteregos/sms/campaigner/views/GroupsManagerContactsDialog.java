package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.services.ContactService;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

/**
 *
 * @author John Emmanuel
 */
public class GroupsManagerContactsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    //Services
    private ContactService contactService;
    //Components
    private javax.swing.JButton addContactsButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JTable contactsTable;
    private java.util.List<Contact> phonebookList;
    private javax.swing.JScrollPane selectContactsScrollPane;
    //Binding
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    //Dtos & helpers
    private Group group;
    private ManageGroupPanel panel;

    /** Creates new form GroupsManagerContactsDialog
     * @param parent
     * @param modal
     * @param group
     * @param panel 
     */
    public GroupsManagerContactsDialog(java.awt.Frame parent, boolean modal, Group group, ManageGroupPanel panel) {
        super(parent, modal);
        this.group = group;
        this.panel = panel;
        initComponents();
        setLocationRelativeTo(parent);
        cleanUpEntries();
    }

    public GroupsManagerContactsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        cleanUpEntries();
    }

    private void cleanUpEntries() {
        Collection<Contact> phoneBookCollection = contactService.getContacts(group);
        List<Contact> toRemove = new ArrayList<Contact>();
        for (Contact phoneBook : phonebookList) {
            if (phoneBookCollection.contains(phoneBook)) {
                toRemove.add(phoneBook);
            }
        }
        phonebookList.removeAll(toRemove);
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task<Boolean, Void> addContactsToGroup() {
        if (contactsTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), "Please select at least one contact!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new AddContactsToGroupTask(org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class), this.panel);
    }

    @Action
    public void closeDialog() {
        this.dispose();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private class AddContactsToGroupTask extends org.jdesktop.application.Task<Boolean, Void> {

        private ManageGroupPanel panel = null;

        AddContactsToGroupTask(org.jdesktop.application.Application app) {
            super(app);
        }

        public AddContactsToGroupTask(org.jdesktop.application.Application app, ManageGroupPanel panel) {
            super(app);
            this.panel = panel;
        }

        @Override
        protected Boolean doInBackground() {
            boolean success = false;
            List<Contact> contactsList = new ArrayList<Contact>();
            int[] selectedRows = contactsTable.getSelectedRows();
            for (int idx = 0; idx < selectedRows.length; idx++) {
                Contact phoneBook = phonebookList.get(contactsTable.convertRowIndexToModel(selectedRows[idx]));
                contactsList.add(phoneBook);
            }

            try {
                contactService.updateGroup(group, contactsList);
                success = true;
            } catch (Exception rollBackException) {
                rollBackException.printStackTrace();
                success = false;
            }

            return success;
        }

        @Override
        protected void succeeded(Boolean result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            boolean success = result;
            if (success) {
                cleanUpEntries();
                this.panel.refreshContactsAction();
                JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), "Contacts added to the SMS Group!", "Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
            }
        }
    }
    //</editor-fold>

    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        contactService = Main.getApplication().getBean("contactService");
        phonebookList = ObservableCollections.observableList(contactService.getContacts());
        selectContactsScrollPane = new javax.swing.JScrollPane();
        contactsTable = new javax.swing.JTable();
        addContactsButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Application application = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(GroupsManagerContactsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        selectContactsScrollPane.setName("selectContactsScrollPane"); // NOI18N

        contactsTable.setName("contactsTable"); // NOI18N

        JTableBinding jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, phonebookList, contactsTable);
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${mobileNo}"));
        columnBinding.setColumnName("Mobile No");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        selectContactsScrollPane.setViewportView(contactsTable);

        ActionMap actionMap = application.getContext().getActionMap(GroupsManagerContactsDialog.class, this);
        addContactsButton.setAction(actionMap.get("addContactsToGroup")); // NOI18N
        addContactsButton.setName("addContactsButton"); // NOI18N

        closeButton.setAction(actionMap.get("closeDialog")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        JPanel contactsPanel = new JPanel(new MigLayout("insets 20 20"));
        Container contentPane = this.getContentPane();
        contentPane.add(contactsPanel);

        contactsPanel.setBorder(BorderFactory.createTitledBorder("Select Contacts"));
        contactsPanel.add(selectContactsScrollPane, "wrap");
        contactsPanel.add(addContactsButton, "split, align right");
        contactsPanel.add(closeButton);

        bindingGroup.bind();

        pack();
    }
}
