package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.util.DateUtils;
import com.alteregos.sms.campaigner.views.helpers.DateColumnCellRenderer;
import com.alteregos.sms.campaigner.views.helpers.MessageTypeColumnCellRenderer;
import com.alteregos.sms.campaigner.views.helpers.PriorityColumnCellRenderer;
import com.alteregos.sms.campaigner.views.helpers.StatusColumnCellRenderer;
import com.alteregos.sms.campaigner.data.dto.OutgoingMessage;
import com.alteregos.sms.campaigner.services.MessageService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class OutboxPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form OutboxPanel */
    public OutboxPanel() {
        initComponents();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void filterAction() {
        String priority = (String) priorityComboBox.getSelectedItem();
        String status = (String) statusComboBox.getSelectedItem();
        String smsType = (String) typeComboBox.getSelectedItem();
        Date startDate = DateUtils.truncateDate(startDateField.getDate());
        Date endDate = DateUtils.getEndOfDay(DateUtils.truncateDate(endDateField.getDate()));
        boolean isEndDateAfterStartDate = endDate.after(startDate);
        if (!isEndDateAfterStartDate) {
            endDate = DateUtils.getEndOfDay(startDate);
            endDateField.setDate(endDate);
        }
        outboxList.addAll(filteredOutboxList);
        filteredOutboxList.clear();
        for (OutgoingMessage sms : outboxList) {
            String actualPriority = sms.getPriority().getMessage();
            String actualStatus = sms.getStatus().getMessage();
            String actualType = sms.getType().getMessage();
            boolean isCreatedDateBeforeStartDate = sms.getCreatedDate().before(startDate);
            boolean isCreatedDateAfterEndDate = sms.getCreatedDate().after(endDate);
            if (!actualPriority.equals(priority) || !actualStatus.endsWith(status) || !actualType.equals(smsType) || isCreatedDateBeforeStartDate || isCreatedDateAfterEndDate) {
                filteredOutboxList.add(sms);
            }
        }
        outboxList.removeAll(filteredOutboxList);
    }

    @Action
    public Task<Object, Void> resendAction() {
        return new ResendSelectedRecords(Main.getApplication());
    }

    @Action
    public Task<Boolean, Void> refreshAction() {
        return new RefreshOutboxActionTask(Main.getApplication());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void initialize() {
        TableColumn priorityColumn = outboxTable.getColumnModel().getColumn(3);
        TableColumn statusColumn = outboxTable.getColumnModel().getColumn(4);
        TableColumn typeColumn = outboxTable.getColumnModel().getColumn(6);
        TableColumn createdDateColumn = outboxTable.getColumnModel().getColumn(7);
        TableColumn sentDateColumn = outboxTable.getColumnModel().getColumn(8);
        priorityColumn.setCellRenderer(new PriorityColumnCellRenderer());
        statusColumn.setCellRenderer(new StatusColumnCellRenderer());
        typeColumn.setCellRenderer(new MessageTypeColumnCellRenderer());
        createdDateColumn.setCellRenderer(new DateColumnCellRenderer());
        sentDateColumn.setCellRenderer(new DateColumnCellRenderer());
        filteredOutboxList = new ArrayList<OutgoingMessage>();
    }

    private class RefreshOutboxActionTask extends Task<Boolean, Void> {

        public RefreshOutboxActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            java.util.Collection<OutgoingMessage> data = messageService.getOutgoingMessages();
            if (outboxList == null) {
                outboxList = ObservableCollections.observableList(new ArrayList<OutgoingMessage>());
            }
            outboxList.clear();
            outboxList.addAll(data);
            return true;
        }

        @Override
        protected void succeeded(Boolean arg0) {
            super.succeeded(arg0);
            outboxTable.getSelectionModel().clearSelection();
            outboxTable.clearSelection();
        }
    }

    private class ResendSelectedRecords extends Task<Object, Void> {

        public ResendSelectedRecords(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        messageService = Main.getApplication().getBean("messageService");

        outboxList = ObservableCollections.observableList(messageService.getOutgoingMessages());
        resendButton = new javax.swing.JButton();
        priorityLabel = new javax.swing.JLabel();
        priorityComboBox = new javax.swing.JComboBox();
        statusLabel = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox();
        typeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        outboxScrollPane = new javax.swing.JScrollPane();
        outboxTable = new javax.swing.JTable();
        filterButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        startDateLabel = new javax.swing.JLabel();
        startDateField = new JXDatePicker();
        endDateLabel = new javax.swing.JLabel();
        endDateField = new JXDatePicker();

        setName("Form"); // NOI18N

        ResourceMap resourceMap = Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(OutboxPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("outboxPanel.border.title"))); // NOI18N
        this.setName("outboxPanel"); // NOI18N

        javax.swing.ActionMap actionMap = Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(OutboxPanel.class, this);
        resendButton.setAction(actionMap.get("resendAction")); // NOI18N
        resendButton.setText(resourceMap.getString("resendButton.text")); // NOI18N
        resendButton.setName("resendButton"); // NOI18N

        priorityLabel.setText(resourceMap.getString("priorityLabel.text")); // NOI18N
        priorityLabel.setName("priorityLabel"); // NOI18N

        priorityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Low", "Normal", "High"}));
        priorityComboBox.setSelectedIndex(1);
        priorityComboBox.setName("priorityComboBox"); // NOI18N

        statusLabel.setText(resourceMap.getString("statusLabel.text")); // NOI18N
        statusLabel.setName("statusLabel"); // NOI18N

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Not Sent", "Queued", "Successfully Sent", "Failed"}));
        statusComboBox.setName("statusComboBox"); // NOI18N

        typeLabel.setText(resourceMap.getString("typeLabel.text")); // NOI18N
        typeLabel.setName("typeLabel"); // NOI18N

        typeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bulk", "Auto Reply"}));
        typeComboBox.setName("typeComboBox"); // NOI18N

        outboxScrollPane.setName("outboxScrollPane"); // NOI18N

        outboxTable.setName("outboxTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, outboxList, outboxTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${refNo}"));
        columnBinding.setColumnName("Ref No");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${recepient}"));
        columnBinding.setColumnName("Recepient");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${content}"));
        columnBinding.setColumnName("Content");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${priority}"));
        columnBinding.setColumnName("Priority");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${errors}"));
        columnBinding.setColumnName("Errors");
        columnBinding.setColumnClass(Short.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${type}"));
        columnBinding.setColumnName("Type");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${createdDate}"));
        columnBinding.setColumnName("Created Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sentDate}"));
        columnBinding.setColumnName("Sent Date");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        outboxScrollPane.setViewportView(outboxTable);

        filterButton.setAction(actionMap.get("filterAction")); // NOI18N
        filterButton.setText(resourceMap.getString("filterButton.text")); // NOI18N
        filterButton.setName("filterButton"); // NOI18N

        refreshButton.setAction(actionMap.get("refreshAction")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        startDateLabel.setText(resourceMap.getString("startDateLabel.text")); // NOI18N
        startDateLabel.setName("startDateLabel"); // NOI18N

        startDateField.setName("startDateField"); // NOI18N

        endDateLabel.setText(resourceMap.getString("endDateLabel.text")); // NOI18N
        endDateLabel.setName("endDateLabel"); // NOI18N

        endDateField.setName("endDateField"); // NOI18N

        this.setLayout(new MigLayout("fill, insets panel", "[min!][][min!][][min!][]", "[min!][min!][grow][min!]"));
                
        this.add(priorityLabel, "span, width 50!, split 6");
        this.add(priorityComboBox, "width 150!, gapright 20");
        this.add(statusLabel, "width 50!");
        this.add(statusComboBox, "width 150!, gapright 20");
        this.add(typeLabel, "width 50!");
        this.add(typeComboBox, "width 150!, wrap");
        this.add(startDateLabel, "span, width 50!, split 6");
        this.add(startDateField, "width 150!, gapright 20");
        this.add(endDateLabel, "width 50!");
        this.add(endDateField, "width 150!, gapright 20");
        this.add(new JLabel(""), "width 50!");
        this.add(filterButton, "wrap");
        this.add(outboxScrollPane, "span, grow, wrap");
        this.add(resendButton, "span, split 2, right");
        this.add(refreshButton);

        bindingGroup.bind();
    }
    private MessageService messageService;
    private JXDatePicker startDateField;
    private JXDatePicker endDateField;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JButton filterButton;
    private java.util.List<OutgoingMessage> outboxList;
    private javax.swing.JScrollPane outboxScrollPane;
    private javax.swing.JTable outboxTable;
    private javax.swing.JComboBox priorityComboBox;
    private javax.swing.JLabel priorityLabel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton resendButton;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JComboBox statusComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JComboBox typeComboBox;
    private javax.swing.JLabel typeLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private List<OutgoingMessage> filteredOutboxList;
}
