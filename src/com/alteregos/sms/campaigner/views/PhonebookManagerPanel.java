package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.services.ContactService;
import java.util.ArrayList;
import java.util.List;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.jdesktop.observablecollections.ObservableCollections;

/**
 *
 * @author  John Emmanuel
 */
public class PhonebookManagerPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private ContactService contactService;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JScrollPane addressScrollPane;
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JScrollPane phoneBookScrollPane;
    private javax.swing.JLabel mobileNoLabel;
    private javax.swing.JTextField mobileNoTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTable phoneBookTable;
    private java.util.List<Contact> phonebookList;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    /** Creates new form PhonebookManagerPanel */
    public PhonebookManagerPanel() {
        initComponents();
        init();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task<Boolean, Void> saveEntryAction() {
        return new SaveEntryActionTask(Main.getApplication());
    }

    @Action
    public Task<Boolean, Void> deleteEntryAction() {
        return new DeleteEntryActionTask(Main.getApplication());
    }

    @Action
    public Task<Boolean, Void> refreshEntriesAction() {
        return new RefreshEntriesActionTask(Main.getApplication());
    }
    //</editor-fold>

    /**
     *
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void init() {
        if (phoneBookTable.getCellEditor() != null) {
            phoneBookTable.getCellEditor().stopCellEditing();
        }
    }

    private class SaveEntryActionTask extends org.jdesktop.application.Task<Boolean, Void> {

        SaveEntryActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SaveEntryTask fields, here.
            super(app);
        }

        @Override
        protected Boolean doInBackground() {
            boolean saved = false;
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            int[] selected = phoneBookTable.getSelectedRows();
            List<Contact> toSave = new ArrayList<Contact>(selected.length);
            for (int index = 0; index < selected.length; index++) {
                Contact p = phonebookList.get(phoneBookTable.convertRowIndexToModel(selected[index]));
                toSave.add(p);
            }

            try {
                contactService.updateContacts(toSave);
                saved = true;
            } catch (Exception rex) {
                rex.printStackTrace();
                saved = false;
            }
            return saved;  // return your result
        }

        @Override
        protected void succeeded(Boolean result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            boolean saved = result;
            if (saved) {
                phoneBookTable.getSelectionModel().clearSelection();
            } else {
                //TODO Let the user know that the entries were not saved
                refreshEntriesAction();
            }
        }
    }

    private class DeleteEntryActionTask extends org.jdesktop.application.Task<Boolean, Void> {

        DeleteEntryActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to DeleteEntryTask fields, here.
            super(app);
        }

        @Override
        protected Boolean doInBackground() {
            Boolean deleted = false;
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            int[] selected = phoneBookTable.getSelectedRows();
            List<Contact> toRemove = new ArrayList<Contact>(selected.length);
            for (int index = 0; index < selected.length; index++) {
                Contact p = phonebookList.get(phoneBookTable.convertRowIndexToModel(selected[index]));
                toRemove.add(p);
            }

            try {
                contactService.deleteContacts(toRemove);
                deleted = phonebookList.removeAll(toRemove);
            } catch (Exception rex) {
                rex.printStackTrace();
                deleted = false;
            }
            return deleted;  // return your result
        }

        @Override
        protected void succeeded(Boolean result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            boolean deleted = result;
            if (deleted) {
                phoneBookTable.getSelectionModel().clearSelection();
                phoneBookTable.clearSelection();
            } else {
                //TODO Show error message to user
                refreshEntriesAction();
            }
        }
    }

    private class RefreshEntriesActionTask extends org.jdesktop.application.Task<Boolean, Void> {

        RefreshEntriesActionTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Boolean doInBackground() {
            java.util.Collection<Contact> data = contactService.getContacts();
            if (phonebookList == null) {
                phonebookList = ObservableCollections.observableList(new ArrayList<Contact>());
            }
            phonebookList.clear();
            phonebookList.addAll(data);
            return true;  // return your result
        }

        @Override
        protected void succeeded(Boolean result) {
            phoneBookTable.getSelectionModel().clearSelection();
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        contactService = Main.getApplication().getBean("contactService");

        phonebookList = ObservableCollections.observableList(contactService.getContacts());
        phoneBookScrollPane = new javax.swing.JScrollPane();
        phoneBookTable = new javax.swing.JTable();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        mobileNoLabel = new javax.swing.JLabel();
        mobileNoTextField = new javax.swing.JTextField();
        emailLabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        addressLabel = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        addressScrollPane = new javax.swing.JScrollPane();
        addressTextArea = new javax.swing.JTextArea();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(PhonebookManagerPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("phoneBookManagerContainer.border.title"))); // NOI18N
        this.setName("phoneBookManagerContainer"); // NOI18N

        phoneBookScrollPane.setName("masterScrollPane"); // NOI18N

        phoneBookTable.setName("phoneBookTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phonebookList, phoneBookTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${mobileNo}"));
        columnBinding.setColumnName("Mobile No");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${email}"));
        columnBinding.setColumnName("Email");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${address}"));
        columnBinding.setColumnName("Address");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        phoneBookScrollPane.setViewportView(phoneBookTable);
        phoneBookTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("phoneBookTable.columnModel.title0")); // NOI18N
        phoneBookTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("phoneBookTable.columnModel.title1")); // NOI18N
        phoneBookTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("phoneBookTable.columnModel.title2")); // NOI18N
        phoneBookTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("phoneBookTable.columnModel.title3")); // NOI18N

        nameLabel.setText(resourceMap.getString("nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        nameTextField.setName("nameTextField"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.name}"), nameTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), nameTextField, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        mobileNoLabel.setText(resourceMap.getString("mobileNoLabel.text")); // NOI18N
        mobileNoLabel.setName("mobileNoLabel"); // NOI18N

        mobileNoTextField.setName("mobileNoTextField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.mobileNo}"), mobileNoTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), mobileNoTextField, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        emailLabel.setText(resourceMap.getString("emailLabel.text")); // NOI18N
        emailLabel.setName("emailLabel"); // NOI18N

        emailTextField.setName("emailTextField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.email}"), emailTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), emailTextField, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        addressLabel.setText(resourceMap.getString("addressLabel.text")); // NOI18N
        addressLabel.setName("addressLabel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(PhonebookManagerPanel.class, this);
        refreshButton.setAction(actionMap.get("refreshEntriesAction")); // NOI18N
        refreshButton.setText(resourceMap.getString("refreshButton.text")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        saveButton.setAction(actionMap.get("saveEntryAction")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        deleteButton.setAction(actionMap.get("deleteEntryAction")); // NOI18N
        deleteButton.setText(resourceMap.getString("deleteButton.text")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), deleteButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        addressScrollPane.setName("addressScrollPane"); // NOI18N

        addressTextArea.setColumns(20);
        addressTextArea.setFont(resourceMap.getFont("addressTextArea.font")); // NOI18N
        addressTextArea.setLineWrap(true);
        addressTextArea.setRows(5);
        addressTextArea.setTabSize(1);
        addressTextArea.setWrapStyleWord(true);
        addressTextArea.setName("addressTextArea"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.address}"), addressTextArea, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, phoneBookTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), addressTextArea, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        addressScrollPane.setViewportView(addressTextArea);

        this.setLayout(new MigLayout("fill, insets panel", "[min!][grow][]", "[grow][min!][min!][min!][][min!]"));
        this.add(phoneBookScrollPane, "spanx 3, grow, push, wrap");
        this.add(nameLabel);
        this.add(nameTextField, "spanx 2, grow, wrap");
        this.add(mobileNoLabel);
        this.add(mobileNoTextField, "spanx 2, grow, wrap");
        this.add(emailLabel);
        this.add(emailTextField, "spanx 2, grow, wrap");
        this.add(addressLabel, "top");
        this.add(addressScrollPane, "spanx 2, grow, height 100!, wrap");
        this.add(saveButton, "spanx 3, split 3, right");
        this.add(deleteButton);
        this.add(refreshButton);

        bindingGroup.bind();
    }
}
