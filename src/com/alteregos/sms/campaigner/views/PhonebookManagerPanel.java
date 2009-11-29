package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.beans.Phonebook;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.RollbackException;
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

    /** Creates new form PhonebookManagerPanel */
    public PhonebookManagerPanel() {
        initComponents();
        init();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task saveEntryAction() {
        return new SaveEntryActionTask(Main.getApplication());
    }

    @Action
    public Task deleteEntryAction() {
        return new DeleteEntryActionTask(Main.getApplication());
    }

    @Action
    public Task refreshEntriesAction() {
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

    private class SaveEntryActionTask extends org.jdesktop.application.Task<Object, Void> {

        SaveEntryActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SaveEntryTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            boolean saved = false;
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            try {
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
                saved = true;
            } catch (RollbackException rex) {
                //TODO DOES THIS ROllBACK WORK WELL?
                rex.printStackTrace();
                entityManager.getTransaction().begin();
                List<com.alteregos.sms.campaigner.data.beans.Phonebook> merged = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(phonebookList.size());
                for (com.alteregos.sms.campaigner.data.beans.Phonebook p : phonebookList) {
                    merged.add(entityManager.merge(p));
                }
                phonebookList.clear();
                phonebookList.addAll(merged);
                saved = false;
            }
            return saved;  // return your result

        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            boolean saved = (Boolean) result;
            if (saved) {
                phoneBookTable.getSelectionModel().clearSelection();
            } else {
                //Show dialog or error message
            }
        }
    }

    private class DeleteEntryActionTask extends org.jdesktop.application.Task<Object, Void> {

        DeleteEntryActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to DeleteEntryTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            Boolean deleted = false;
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            int[] selected = phoneBookTable.getSelectedRows();
            List<com.alteregos.sms.campaigner.data.beans.Phonebook> toRemove = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(selected.length);
            for (int index = 0; index < selected.length; index++) {
                com.alteregos.sms.campaigner.data.beans.Phonebook p = phonebookList.get(phoneBookTable.convertRowIndexToModel(selected[index]));
                toRemove.add(p);
                entityManager.remove(p);
            }

            try {
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
                deleted = phonebookList.removeAll(toRemove);
            } catch (RollbackException rex) {
                //TODO VERIFY IF MERGING IS DONE CORRECTLY
                rex.printStackTrace();
                List<com.alteregos.sms.campaigner.data.beans.Phonebook> merged = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(phonebookList.size());
                for (com.alteregos.sms.campaigner.data.beans.Phonebook p : phonebookList) {
                    merged.add(entityManager.merge(p));
                }
                phonebookList.clear();
                phonebookList.addAll(merged);
                deleted = false;
            }
            return deleted;  // return your result

        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            boolean deleted = (Boolean) result;
            if (deleted) {
                phoneBookTable.getSelectionModel().clearSelection();
                phoneBookTable.clearSelection();
            } else {
            }
        }
    }

    private class RefreshEntriesActionTask extends org.jdesktop.application.Task<Object, Void> {

        RefreshEntriesActionTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            entityManager.getTransaction().begin();
            entityManager.getTransaction().rollback();
            java.util.Collection data = phonebookQuery.getResultList();
            for (Object entity : data) {
                entityManager.refresh(entity);
            }
            if (phonebookList != null) {
                phonebookList.clear();
            } else {
                phonebookList = ObservableCollections.observableList(new ArrayList<Phonebook>());
            }
            phonebookList.addAll(data);

            return null;  // return your result

        }

        @Override
        protected void succeeded(Object result) {
            phoneBookTable.getSelectionModel().clearSelection();
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        phonebookQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT p FROM Phonebook p");
        phonebookList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(phonebookQuery.getResultList());
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
    private javax.swing.JLabel addressLabel;
    private javax.swing.JScrollPane addressScrollPane;
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailTextField;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JScrollPane phoneBookScrollPane;
    private javax.swing.JLabel mobileNoLabel;
    private javax.swing.JTextField mobileNoTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTable phoneBookTable;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Phonebook> phonebookList;
    private javax.persistence.Query phonebookQuery;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
}
