package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.IncomingMessage;
import com.alteregos.sms.campaigner.services.MessageService;
import com.alteregos.sms.campaigner.util.DateUtils;
import com.alteregos.sms.campaigner.views.helpers.DateColumnCellRenderer;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class InboxPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form InboxPanel */
    public InboxPanel() {
        initComponents();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void filterAction() {
        inboxList.addAll(filteredInboxList);
        filteredInboxList.clear();
        Date startDate = DateUtils.truncateDate(startDateField.getDate());
        Date endDate = DateUtils.getEndOfDay(DateUtils.truncateDate(endDateField.getDate()));
        boolean isEndDateAfterStartDate = endDate.after(startDate);
        if (!isEndDateAfterStartDate) {
            endDate = DateUtils.getEndOfDay(startDate);
            endDateField.setDate(endDate);
        }
        for (int i = 0; i < inboxList.size(); i++) {
            IncomingMessage inbox = inboxList.get(i);
            boolean isReceiptDateBeforeStartDate = inbox.getReceiptDate().before(startDate);
            boolean isReceiptDateAfterEndDate = inbox.getReceiptDate().after(endDate);
            if (isReceiptDateBeforeStartDate || isReceiptDateAfterEndDate) {
                filteredInboxList.add(inbox);
            }
        }
        inboxList.removeAll(filteredInboxList);
    }

    @Action
    public Task<Boolean, Void> refreshAction() {
        return new RefreshListTask(Main.getApplication());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void initialize() {
        TableColumn receiptDateColumn = inboxTable.getColumnModel().getColumn(2);
        TableColumn messageDateColumn = inboxTable.getColumnModel().getColumn(3);
        receiptDateColumn.setCellRenderer(new DateColumnCellRenderer());
        messageDateColumn.setCellRenderer(new DateColumnCellRenderer());
        filteredInboxList = new ArrayList<IncomingMessage>();
    }

    private class RefreshListTask extends Task<Boolean, Void> {

        public RefreshListTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            java.util.Collection<IncomingMessage> data = messageService.getIncomingMessages();
            if (inboxList == null) {
                inboxList = ObservableCollections.observableList(new ArrayList<IncomingMessage>());
            }
            inboxList.clear();
            inboxList.addAll(data);
            return true;
        }

        @Override
        protected void succeeded(Boolean arg0) {
            super.succeeded(arg0);
            filteredInboxList.clear();
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        messageService = Main.getApplication().getBean("messageService");

        inboxList = ObservableCollections.observableList(messageService.getIncomingMessages());
        inboxScrollPane = new javax.swing.JScrollPane();
        inboxTable = new javax.swing.JTable();
        startDateLabel = new javax.swing.JLabel();
        startDateField = new JXDatePicker();
        endDateLabel = new javax.swing.JLabel();
        endDateField = new JXDatePicker();
        filterButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        Main application = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(InboxPanel.class);
        this.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("inboxPanel.border.title"))); // NOI18N
        this.setName("inboxPanel"); // NOI18N

        inboxScrollPane.setName("inboxScrollPane"); // NOI18N

        inboxTable.setName("inboxTable"); // NOI18N

        JTableBinding jTableBinding = SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, inboxList, inboxTable);
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sender}"));
        columnBinding.setColumnName("Sender");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${content}"));
        columnBinding.setColumnName("Content");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${receiptDate}"));
        columnBinding.setColumnName("Receipt Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${messageDate}"));
        columnBinding.setColumnName("Message Date");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        inboxScrollPane.setViewportView(inboxTable);

        startDateLabel.setText(resourceMap.getString("startDateLabel.text")); // NOI18N
        startDateLabel.setName("startDateLabel"); // NOI18N

        startDateField.setName("startDateField"); // NOI18N

        endDateLabel.setText(resourceMap.getString("endDateLabel.text")); // NOI18N
        endDateLabel.setName("endDateLabel"); // NOI18N

        endDateField.setName("endDateField"); // NOI18N

        ActionMap actionMap = application.getContext().getActionMap(InboxPanel.class, this);
        filterButton.setAction(actionMap.get("filterAction")); // NOI18N
        filterButton.setText(resourceMap.getString("filterButton.text")); // NOI18N
        filterButton.setName("filterButton"); // NOI18N

        refreshButton.setAction(actionMap.get("refreshAction")); // NOI18N
        refreshButton.setText(resourceMap.getString("refreshButton.text")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        Dimension dimension = new Dimension(100, startDateLabel.getHeight());
        startDateField.setPreferredSize(dimension);
        endDateField.setPreferredSize(dimension);

        this.setLayout(new MigLayout("insets panel", "[min!][grow,push]30[min!][grow,push]30[min!]", "[min!][][]"));

        this.add(startDateLabel);
        this.add(startDateField, "grow, push");
        this.add(endDateLabel);
        this.add(endDateField, "grow, push");
        this.add(filterButton, "wrap");

        this.add(inboxScrollPane, "spanx 5, grow, push, wrap");
        this.add(refreshButton, "spanx 5, right");

        bindingGroup.bind();
    }
    private MessageService messageService;
    private JXDatePicker startDateField;
    private JXDatePicker endDateField;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JButton filterButton;
    private javax.swing.JScrollPane inboxScrollPane;
    private javax.swing.JTable inboxTable;
    private javax.swing.JButton refreshButton;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private java.util.List<IncomingMessage> inboxList;
    private List<IncomingMessage> filteredInboxList;
}
