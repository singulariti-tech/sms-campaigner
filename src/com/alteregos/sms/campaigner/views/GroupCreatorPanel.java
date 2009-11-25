package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.beans.Phonebook;
import com.alteregos.sms.campaigner.data.beans.Smsgroup;
import com.alteregos.sms.campaigner.data.validation.GroupValidator;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.ResultMessage;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.exceptions.UnsuccessfulTaskResult;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.RollbackException;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.jdesktop.swingbinding.SwingBindings;

/**
 *
 * @author  John Emmanuel
 */
public class GroupCreatorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form GroupCreatorPanel */
    public GroupCreatorPanel() {
        initComponents();
        initialize();
    }

    private void initialize() {
        groupMembersList = ObservableCollections.observableList(new ArrayList<Phonebook>());
        JTableBinding groupMemberTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, groupMembersList, groupMembersTable);
        ColumnBinding nameColumnBinding = groupMemberTableBinding.addColumnBinding(ELProperty.create("${name}"));
        nameColumnBinding.setColumnName("Name");
        nameColumnBinding.setColumnClass(String.class);
        ColumnBinding mobileNoBinding = groupMemberTableBinding.addColumnBinding(ELProperty.create("${mobileNo}"));
        mobileNoBinding.setColumnName("Mobile No.");
        mobileNoBinding.setColumnClass(String.class);
        groupMemberTableBinding.bind();
        validator = new GroupValidator(groupNameField, groupMembersTable);
    }
    //<editor-fold defaultstate="collapsed" desc="Actions">

    @Action
    public Task clearDataAction() {
        return new ClearDataActionTask(Main.getApplication());
    }

    @Action
    public Task createGroupAction() {
        if (validator.validate()) {
            return new CreateGroupActionTask(Main.getApplication());
        }
        return null;
    }

    @Action
    public void addAllAction() {
        int noRows = phoneBookTable.getRowCount();
        List<com.alteregos.sms.campaigner.data.beans.Phonebook> forMembership = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(noRows);
        for (int index = 0; index < noRows; index++) {
            Phonebook phoneBook = phonebookList.get(phoneBookTable.convertRowIndexToModel(index));
            forMembership.add(phoneBook);
        }
        phonebookList.removeAll(forMembership);
        groupMembersList.addAll(forMembership);
        //groupMembersTable.setBackground(new Color(255, 255, 255));
    }

    @Action
    public void addSelectedAction() {
        int[] selected = phoneBookTable.getSelectedRows();
        List<com.alteregos.sms.campaigner.data.beans.Phonebook> forMembership = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(selected.length);
        for (int index = 0; index < selected.length; index++) {
            Phonebook phoneBook = phonebookList.get(
                    phoneBookTable.convertRowIndexToModel(selected[index]));
            forMembership.add(phoneBook);
        }
        phonebookList.removeAll(forMembership);
        groupMembersList.addAll(forMembership);
        //groupMembersTable.setBackground(new Color(255, 255, 255));
    }

    @Action
    public void removeAllAction() {
        int noRows = groupMembersTable.getRowCount();
        List<com.alteregos.sms.campaigner.data.beans.Phonebook> toRemove = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(noRows);
        for (int index = 0; index < noRows; index++) {
            Phonebook phoneBook = groupMembersList.get(groupMembersTable.convertRowIndexToModel(index));
            toRemove.add(phoneBook);
        }
        groupMembersList.removeAll(toRemove);
        phonebookList.addAll(toRemove);
    }

    @Action
    public void removeSelectedAction() {
        int[] selected = groupMembersTable.getSelectedRows();
        List<com.alteregos.sms.campaigner.data.beans.Phonebook> toRemove = new ArrayList<com.alteregos.sms.campaigner.data.beans.Phonebook>(selected.length);
        for (int index = 0; index < selected.length; index++) {
            Phonebook phoneBook = groupMembersList.get(
                    groupMembersTable.convertRowIndexToModel(selected[index]));
            toRemove.add(phoneBook);
        }
        groupMembersList.removeAll(toRemove);
        phonebookList.addAll(toRemove);
    }
    //</editor-fold>

    /**
     *
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private class ClearDataActionTask extends Task {

        public ClearDataActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
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
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
            groupNameField.setText("");
            groupMembersList.clear();
        }
    }

    private class CreateGroupActionTask
            extends Task {

        public CreateGroupActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
            ITaskResult result = null;
            String groupName = groupNameField.getText();
            int rowCount = groupMembersTable.getRowCount();

            if (groupName.length() > 0 && rowCount > 0) {
                Smsgroup group = new Smsgroup();
                group.setName(groupName);
                group.setPhoneBookCollection(groupMembersList);

                try {
                    entityManager.getTransaction().begin();
                    entityManager.persist(group);
                    entityManager.getTransaction().commit();
                    result = new SuccessfulTaskResult();
                } catch (RollbackException rollBackException) {
                    log.error("Error when creating group");
                    log.error(rollBackException);
                    result = ExceptionParser.getError(rollBackException);
                }
            } else {
                result = new UnsuccessfulTaskResult(ResultMessage.VALIDATION_FAILED);
            }
            return result;
        }

        @Override
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
            ITaskResult result = (ITaskResult) arg0;
            if (result.isSuccessful()) {
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
                groupNameField.setText("");
                groupMembersList.clear();
                JOptionPane.showMessageDialog(
                        Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());

            } else {
                log.info("Group could not be created. Reason - " + result.getResultMessage().getLabel());
                //TODO SHOULD WE SHOW THE MESSAGE DIALOG
                //JOptionPane.showMessageDialog(
                //        Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());
            }
        }
    }
    //</editor-fold>

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        phonebookQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT p FROM Phonebook p");
        phonebookList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(phonebookQuery.getResultList());
        borderContainer = new javax.swing.JPanel();
        groupNameLabel = new javax.swing.JLabel();
        groupNameField = new javax.swing.JTextField();
        phoneBookLabel = new javax.swing.JLabel();
        phoneBookScrollPane = new javax.swing.JScrollPane();
        phoneBookTable = new javax.swing.JTable();
        groupMembersLabel = new javax.swing.JLabel();
        groupMembersScrollPane = new javax.swing.JScrollPane();
        groupMembersTable = new javax.swing.JTable();
        addSelectedButton = new javax.swing.JButton();
        addAllButton = new javax.swing.JButton();
        removeSelectedButton = new javax.swing.JButton();
        removeAllButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        Main application = Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(GroupCreatorPanel.class);
        borderContainer.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N
        borderContainer.setName("borderContainer"); // NOI18N

        groupNameLabel.setText(resourceMap.getString("groupNameLabel.text")); // NOI18N
        groupNameLabel.setName("groupNameLabel"); // NOI18N

        groupNameField.setText(resourceMap.getString("groupNameField.text")); // NOI18N
        groupNameField.setName("groupNameField"); // NOI18N

        phoneBookLabel.setText(resourceMap.getString("phoneBookLabel.text")); // NOI18N
        phoneBookLabel.setName("phoneBookLabel"); // NOI18N

        phoneBookScrollPane.setName("phoneBookScrollPane"); // NOI18N

        phoneBookTable.setName("phoneBookTable"); // NOI18N

        JTableBinding jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, phonebookList, phoneBookTable);
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${mobileNo}"));
        columnBinding.setColumnName("Mobile No");
        columnBinding.setColumnClass(String.class);
        jTableBinding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        phoneBookScrollPane.setViewportView(phoneBookTable);

        groupMembersLabel.setText(resourceMap.getString("groupMembersLabel.text")); // NOI18N
        groupMembersLabel.setName("groupMembersLabel"); // NOI18N

        groupMembersScrollPane.setName("groupMembersScrollPane"); // NOI18N

        groupMembersTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Name", "Mobile No."
                }) {

            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        groupMembersTable.setName("groupMembersTable"); // NOI18N
        groupMembersScrollPane.setViewportView(groupMembersTable);
        groupMembersTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("groupMembersTable.columnModel.title0")); // NOI18N
        groupMembersTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("groupMembersTable.columnModel.title1")); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(GroupCreatorPanel.class, this);
        addSelectedButton.setAction(actionMap.get("addAllAction")); // NOI18N
        addSelectedButton.setName("addSelectedButton"); // NOI18N

        addAllButton.setAction(actionMap.get("addSelectedAction")); // NOI18N
        addAllButton.setName("addAllButton"); // NOI18N

        removeSelectedButton.setAction(actionMap.get("removeSelectedAction")); // NOI18N
        removeSelectedButton.setName("removeSelectedButton"); // NOI18N

        removeAllButton.setAction(actionMap.get("removeAllAction")); // NOI18N
        removeAllButton.setName("removeAllButton"); // NOI18N

        clearButton.setAction(actionMap.get("clearDataAction")); // NOI18N
        clearButton.setName("clearButton"); // NOI18N

        createButton.setAction(actionMap.get("createGroupAction")); // NOI18N
        createButton.setName("createButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets 20 20"));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder("Create New Group"));

        this.add(groupNameLabel);
        this.add(groupNameField, "grow, wrap");
        this.add(phoneBookLabel, "gaptop 10");
        this.add(groupMembersLabel, "skip 2, wrap");
        this.add(phoneBookScrollPane, "spanx 2");
        this.add(addAllButton, "flowy, cell 2 2, top");
        this.add(addSelectedButton, "cell 2 2");
        this.add(removeSelectedButton, "cell 2 2");
        this.add(removeAllButton, "cell 2 2");
        this.add(groupMembersScrollPane, "spanx 2, wrap");
        this.add(createButton, "skip 3, split 2, align right");
        this.add(clearButton, "align right");

        bindingGroup.bind();
    }
    private javax.swing.JButton addAllButton;
    private javax.swing.JButton addSelectedButton;
    private javax.swing.JPanel borderContainer;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton createButton;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JLabel groupMembersLabel;
    private javax.swing.JScrollPane groupMembersScrollPane;
    private javax.swing.JTable groupMembersTable;
    private javax.swing.JTextField groupNameField;
    private javax.swing.JLabel groupNameLabel;
    private javax.swing.JLabel phoneBookLabel;
    private javax.swing.JScrollPane phoneBookScrollPane;
    private javax.swing.JTable phoneBookTable;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Phonebook> phonebookList;
    private javax.persistence.Query phonebookQuery;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Phonebook> groupMembersList;
    private GroupValidator validator;
    private static Logger log = LoggerHelper.getLogger();
}
