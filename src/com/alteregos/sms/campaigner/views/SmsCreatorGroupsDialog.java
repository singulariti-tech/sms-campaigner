package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.services.ContactService;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;

/**
 *
 * @author  John Emmanuel
 */
public class SmsCreatorGroupsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    /** Creates new form SmsCreatorGroupsDialog
     * @param parent
     * @param modal
     */
    public SmsCreatorGroupsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SmsCreatorGroupsDialog(Frame owner, boolean modal, SmsSenderPanel panel, List<String> recepients) {
        this(owner, modal);
        this.panel = panel;
        this.recepients = recepients;
        filterGroups(smsgroupList);
    }

    private List<String> filterGroups(List<Group> groupsList) {
        List<Group> filteredGroups = new ArrayList<Group>();
        for (Group group : groupsList) {
            boolean missed = false;
            Collection<Contact> phoneBookCollection = contactService.getContacts(group);
            Iterator<Contact> iterator = phoneBookCollection.iterator();
            while (iterator.hasNext()) {
                Contact contact = iterator.next();
                String number = contact.getMobileNo();
                if (!this.recepients.contains(number)) {
                    missed = true;
                }
            }

            if (!missed) {
                filteredGroups.add(group);
            }
        }

        this.smsgroupList.removeAll(filteredGroups);
        return this.recepients;
    }

    private List<String> filterRecepients(List<Group> groupsList) {
        List<String> finalRecepients = new ArrayList<String>();
        for (Group group : groupsList) {
            Collection<Contact> phoneBookCollection = contactService.getContacts(group);
            Iterator<Contact> iterator = phoneBookCollection.iterator();
            while (iterator.hasNext()) {
                Contact book = iterator.next();
                String number = book.getMobileNo();
                if (!this.recepients.contains(number)) {
                    finalRecepients.add(number);
                }
            }
        }

        finalRecepients.addAll(this.recepients);
        return finalRecepients;
    }

    @Action
    public void addRecepientsAction() {
        int[] selectedRows = groupsTable.getSelectedRows();
        List<Group> selectedGroupsList = new ArrayList<Group>();
        for (int index = 0; index < selectedRows.length; index++) {
            Group group = smsgroupList.get(groupsTable.convertRowIndexToModel(selectedRows[index]));
            selectedGroupsList.add(group);
        }

        List<String> finalRecepientList = filterRecepients(selectedGroupsList);
        this.panel.setRecepients(finalRecepientList);
        this.dispose();
    }

    @Action
    public void closeAction() {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        contactService = Main.getApplication().getBean("contactService");
        smsgroupList = ObservableCollections.observableList(contactService.getGroups());
        borderContainer = new javax.swing.JPanel();
        groupsScrollPane = new javax.swing.JScrollPane();
        groupsTable = new javax.swing.JTable();
        addRecepientsButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(SmsCreatorGroupsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(320, 240));
        setName("Form"); // NOI18N
        setResizable(false);

        borderContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N
        borderContainer.setName("borderContainer"); // NOI18N

        groupsScrollPane.setName("groupsScrollPane"); // NOI18N

        groupsTable.setName("groupsTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, smsgroupList, groupsTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        groupsScrollPane.setViewportView(groupsTable);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(SmsCreatorGroupsDialog.class, this);
        addRecepientsButton.setAction(actionMap.get("addRecepientsAction")); // NOI18N
        addRecepientsButton.setText(resourceMap.getString("addRecepientsButton.text")); // NOI18N
        addRecepientsButton.setName("addRecepientsButton"); // NOI18N

        closeButton.setAction(actionMap.get("closeAction")); // NOI18N
        closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets dialog"));
        this.add(borderContainer);

        borderContainer.setLayout(new MigLayout("fill, insets panel"));
        borderContainer.add(groupsScrollPane, "wrap");
        borderContainer.add(addRecepientsButton, "split 2, right");
        borderContainer.add(closeButton);

        bindingGroup.bind();

        pack();
    }
    private ContactService contactService;
    private javax.swing.JButton addRecepientsButton;
    private javax.swing.JPanel borderContainer;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane groupsScrollPane;
    private javax.swing.JTable groupsTable;
    private java.util.List<Group> smsgroupList;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private SmsSenderPanel panel;
    private List<String> recepients;
}
