package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.dto.Group;
import com.alteregos.sms.campaigner.data.validation.GroupValidator;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.ResultMessage;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.exceptions.UnsuccessfulTaskResult;
import com.alteregos.sms.campaigner.services.ContactService;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  John Emmanuel
 */
public class GroupCreatorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupCreatorPanel.class);
    private javax.swing.JButton addAllButton;
    private javax.swing.JButton addSelectedButton;
    private javax.swing.JPanel borderContainer;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton createButton;
    private javax.swing.JLabel groupMembersLabel;
    private javax.swing.JScrollPane groupMembersScrollPane;
    private javax.swing.JTable groupMembersTable;
    private javax.swing.JTextField groupNameField;
    private javax.swing.JLabel groupNameLabel;
    private javax.swing.JLabel phoneBookLabel;
    private javax.swing.JScrollPane phoneBookScrollPane;
    private javax.swing.JTable phoneBookTable;
    private java.util.List<Contact> phonebookList;
    private javax.swing.JButton removeAllButton;
    private javax.swing.JButton removeSelectedButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private java.util.List<Contact> groupMembersList;
    private GroupValidator validator;
    private ContactService contactService;

    /** Creates new form GroupCreatorPanel */
    public GroupCreatorPanel() {
        initComponents();
        initialize();
    }

    private void initialize() {
        groupMembersList = ObservableCollections.observableList(new ArrayList<Contact>());
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
    public Task<Boolean, Void> clearDataAction() {
        return new ClearDataActionTask(Main.getApplication());
    }

    @Action
    public Task<ITaskResult, Void> createGroupAction() {
        if (validator.validate()) {
            return new CreateGroupActionTask(Main.getApplication());
        }
        return null;
    }

    @Action
    public void addAllAction() {
        int noRows = phoneBookTable.getRowCount();
        List<Contact> forMembership = new ArrayList<Contact>(noRows);
        for (int index = 0; index < noRows; index++) {
            Contact phoneBook = phonebookList.get(phoneBookTable.convertRowIndexToModel(index));
            forMembership.add(phoneBook);
        }
        phonebookList.removeAll(forMembership);
        groupMembersList.addAll(forMembership);
        //groupMembersTable.setBackground(new Color(255, 255, 255));
    }

    @Action
    public void addSelectedAction() {
        int[] selected = phoneBookTable.getSelectedRows();
        List<Contact> forMembership = new ArrayList<Contact>(selected.length);
        for (int index = 0; index < selected.length; index++) {
            Contact phoneBook = phonebookList.get(phoneBookTable.convertRowIndexToModel(selected[index]));
            forMembership.add(phoneBook);
        }
        phonebookList.removeAll(forMembership);
        groupMembersList.addAll(forMembership);
        //groupMembersTable.setBackground(new Color(255, 255, 255));
    }

    @Action
    public void removeAllAction() {
        int noRows = groupMembersTable.getRowCount();
        List<Contact> toRemove = new ArrayList<Contact>(noRows);
        for (int index = 0; index < noRows; index++) {
            Contact phoneBook = groupMembersList.get(groupMembersTable.convertRowIndexToModel(index));
            toRemove.add(phoneBook);
        }
        groupMembersList.removeAll(toRemove);
        phonebookList.addAll(toRemove);
    }

    @Action
    public void removeSelectedAction() {
        int[] selected = groupMembersTable.getSelectedRows();
        List<Contact> toRemove = new ArrayList<Contact>(selected.length);
        for (int index = 0; index < selected.length; index++) {
            Contact phoneBook = groupMembersList.get(groupMembersTable.convertRowIndexToModel(selected[index]));
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
    private class ClearDataActionTask extends Task<Boolean, Void> {

        public ClearDataActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            java.util.Collection<Contact> data = contactService.getContacts();
            if (phonebookList == null) {
                phonebookList = ObservableCollections.observableList(new ArrayList<Contact>());
            }
            phonebookList.clear();
            phonebookList.addAll(data);
            return true;  // return your result
        }

        @Override
        protected void succeeded(Boolean arg0) {
            super.succeeded(arg0);
            groupNameField.setText("");
            groupMembersList.clear();
        }
    }

    private class CreateGroupActionTask extends Task<ITaskResult, Void> {

        public CreateGroupActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected ITaskResult doInBackground() throws Exception {
            ITaskResult result = null;
            String groupName = groupNameField.getText();
            int rowCount = groupMembersTable.getRowCount();

            if (groupName.length() > 0 && rowCount > 0) {
                Group group = new Group();
                group.setName(groupName);
                try {
                    contactService.newGroup(group, groupMembersList);
                    result = new SuccessfulTaskResult();
                } catch (Exception rollBackException) {
                    LOGGER.error("-- Error when creating group: {}", rollBackException);
                    result = ExceptionParser.getError(rollBackException);
                }
            } else {
                result = new UnsuccessfulTaskResult(ResultMessage.VALIDATION_FAILED);
            }
            return result;
        }

        @Override
        protected void succeeded(ITaskResult arg0) {
            super.succeeded(arg0);
            ITaskResult result = arg0;
            if (result.isSuccessful()) {
                java.util.Collection<Contact> data = contactService.getContacts();
                if (phonebookList == null) {
                    phonebookList = ObservableCollections.observableList(new ArrayList<Contact>());
                }
                phonebookList.clear();
                phonebookList.addAll(data);
                groupNameField.setText("");
                groupMembersList.clear();
                JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());
            } else {
                LOGGER.info("Group could not be created. Reason - " + result.getResultMessage().getLabel());
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

        contactService = Main.getApplication().getBean("contactService");
        phonebookList = ObservableCollections.observableList(contactService.getContacts());
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

        Main application = Application.getInstance(Main.class);
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

            private static final long serialVersionUID = 1L;
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        groupMembersTable.setName("groupMembersTable"); // NOI18N
        groupMembersScrollPane.setViewportView(groupMembersTable);
        groupMembersTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("groupMembersTable.columnModel.title0")); // NOI18N
        groupMembersTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("groupMembersTable.columnModel.title1")); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(Main.class).getContext().getActionMap(GroupCreatorPanel.class, this);
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

        this.add(groupNameLabel, "span, split 2");
        this.add(groupNameField, "gap, width 150!, wrap");
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
}
