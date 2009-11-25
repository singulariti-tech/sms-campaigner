package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.beans.Inbox;
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
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class InboxPanel extends javax.swing.JPanel {

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
            Inbox inbox = inboxList.get(i);
            boolean isReceiptDateBeforeStartDate = inbox.getReceiptDate().before(startDate);
            boolean isReceiptDateAfterEndDate = inbox.getReceiptDate().after(endDate);
            if (isReceiptDateBeforeStartDate || isReceiptDateAfterEndDate) {
                filteredInboxList.add(inbox);
            }
        }
        inboxList.removeAll(filteredInboxList);
    }

    @Action
    public Task refreshAction() {
        return new RefreshListTask(Main.getApplication());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void initialize() {
        TableColumn receiptDateColumn = inboxTable.getColumnModel().getColumn(2);
        TableColumn messageDateColumn = inboxTable.getColumnModel().getColumn(3);
        receiptDateColumn.setCellRenderer(new DateColumnCellRenderer());
        messageDateColumn.setCellRenderer(new DateColumnCellRenderer());
        filteredInboxList = new ArrayList<Inbox>();
    }

    private class RefreshListTask extends Task<Object, Void> {

        public RefreshListTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
            java.util.Collection data = inboxQuery.getResultList();
            for (Object entity : data) {
                entityManager.refresh(entity);
            }
            if (inboxList != null) {
                inboxList.clear();
            } else {
                inboxList = new ArrayList<Inbox>();
            }
            inboxList.addAll(data);
            return true;
        }

        @Override
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
            filteredInboxList.clear();
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        inboxQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT i FROM Inbox i");
        inboxList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(inboxQuery.getResultList());
        inboxPanel = new javax.swing.JPanel(new MigLayout("debug, fill, insets panel"));
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
        inboxPanel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("inboxPanel.border.title"))); // NOI18N
        inboxPanel.setName("inboxPanel"); // NOI18N

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

        inboxPanel.add(startDateLabel, "width min!");
        inboxPanel.add(startDateField, "gapright 20");
        inboxPanel.add(endDateLabel, "width min!");
        inboxPanel.add(endDateField, "gapright 40");
        inboxPanel.add(filterButton, "wrap");

        inboxPanel.add(inboxScrollPane, "spanx 5, grow, push, wrap");
        inboxPanel.add(refreshButton, "spanx 5, right");

        this.setLayout(new MigLayout("debug, fill, insets panel"));
        this.add(inboxPanel, "grow");
        
        bindingGroup.bind();
    }
    private JXDatePicker endDateField;
    private javax.swing.JLabel endDateLabel;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JButton filterButton;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Inbox> inboxList;
    private javax.swing.JPanel inboxPanel;
    private javax.persistence.Query inboxQuery;
    private javax.swing.JScrollPane inboxScrollPane;
    private javax.swing.JTable inboxTable;
    private javax.swing.JButton refreshButton;
    private JXDatePicker startDateField;
    private javax.swing.JLabel startDateLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private List<Inbox> filteredInboxList;
}
