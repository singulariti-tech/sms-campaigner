package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.services.ContactService;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;

/**
 *
 * @author  John Emmanuel
 */
public class SmsCreatorContactsDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    //Services
    private ContactService contactService;
    //Components
    private javax.swing.JButton addRecepientsButton;
    private javax.swing.JPanel borderContainer;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane contactsScrollPane;
    private javax.swing.JTable contactsTable;
    private SmsSenderPanel panel;
    //Binding
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    //Helpers
    private java.util.List<Contact> phonebookList;
    private List<String> recepients;

    /** Creates new form SmsCreatorContactsDialog
     * @param parent
     * @param modal
     */
    public SmsCreatorContactsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SmsCreatorContactsDialog(Frame owner, boolean modal, SmsSenderPanel panel, List<String> recepients) {
        this(owner, modal);
        this.panel = panel;
        this.recepients = recepients;
        filterContacts(phonebookList);
    }

    private List<Contact> filterContacts(List<Contact> contactsList) {
        List<Contact> allContacts = new ArrayList<Contact>(phonebookList);
        List<Contact> filteredContacts = new ArrayList<Contact>();
        for (Contact contact : contactsList) {
            if (this.recepients.contains(contact.getMobileNo())) {
                filteredContacts.add(contact);
            }
        }

        allContacts.removeAll(filteredContacts);
        phonebookList.removeAll(filteredContacts);
        return allContacts;
    }

    @Action
    public void closeAction() {
        this.dispose();
    }

    @Action
    public void addContactsAction() {
        int[] selectedRows = contactsTable.getSelectedRows();
        List<String> selectedNumbers = new ArrayList<String>();
        for (int index = 0; index < selectedRows.length; index++) {
            Contact contact = phonebookList.get(contactsTable.convertRowIndexToModel(selectedRows[index]));
            String number = contact.getMobileNo();
            if (!number.equals("") && !this.recepients.contains(number)) {
                selectedNumbers.add(number);
            }
        }

        selectedNumbers.addAll(this.recepients);
        this.panel.setRecepients(selectedNumbers);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        contactService = Main.getApplication().getBean("contactService");
        phonebookList = ObservableCollections.observableList(contactService.getContacts());
        borderContainer = new javax.swing.JPanel();
        contactsScrollPane = new javax.swing.JScrollPane();
        contactsTable = new javax.swing.JTable();
        closeButton = new javax.swing.JButton();
        addRecepientsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(SmsCreatorContactsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(320, 240));
        setName("Form"); // NOI18N
        setResizable(false);

        borderContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N
        borderContainer.setName("borderContainer"); // NOI18N

        contactsScrollPane.setName("contactsScrollPane"); // NOI18N

        contactsTable.setName("contactsTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phonebookList, contactsTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${mobileNo}"));
        columnBinding.setColumnName("Mobile No");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        contactsScrollPane.setViewportView(contactsTable);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(SmsCreatorContactsDialog.class, this);
        closeButton.setAction(actionMap.get("closeAction")); // NOI18N
        closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        addRecepientsButton.setAction(actionMap.get("addContactsAction")); // NOI18N
        addRecepientsButton.setText(resourceMap.getString("addRecepientsButton.text")); // NOI18N
        addRecepientsButton.setName("addRecepientsButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets dialog"));
        this.add(borderContainer);

        borderContainer.setLayout(new MigLayout("fill, insets panel"));
        borderContainer.add(contactsScrollPane, "wrap");
        borderContainer.add(addRecepientsButton, "split 2, right");
        borderContainer.add(closeButton);

        bindingGroup.bind();
        pack();
    }
}
