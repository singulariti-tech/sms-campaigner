package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.beans.Calls;
import com.alteregos.sms.campaigner.util.DateUtils;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.views.helpers.DateColumnCellRenderer;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class CallsPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form DndPanel */
    public CallsPanel() {
        initComponents();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void filterAction() {
        Date startDate = DateUtils.truncateDate(startDateField.getDate());
        Date endDate = DateUtils.getEndOfDay(DateUtils.truncateDate(endDateField.getDate()));
        boolean isEndDateAfterStartDate = endDate.after(startDate);
        if (!isEndDateAfterStartDate) {
            endDate = DateUtils.getEndOfDay(startDate);
            endDateField.setDate(endDate);
        }
        callsList.addAll(filteredCalls);
        filteredCalls.clear();
        for (int i = 0; i < callsList.size(); i++) {
            Calls call = callsList.get(i);
            Date receiptDate = call.getReceiptDate();
            boolean isReceiptDateBeforeStartDate = receiptDate.before(startDate);
            boolean isReceiptDateAfterEndDate = receiptDate.after(endDate);
            if (isReceiptDateBeforeStartDate || isReceiptDateAfterEndDate) {
                filteredCalls.add(call);
            }

        }
        callsList.removeAll(filteredCalls);
    }

    @Action
    public Task refreshAction() {
        return new RefreshListTask(Main.getApplication());
    }
    //</editor-fold>

    /**
     *
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void initialize() {
        TableColumn recepitDateColumn = callsTable.getColumnModel().getColumn(1);
        recepitDateColumn.setCellRenderer(new DateColumnCellRenderer());
        filteredCalls = new ArrayList<Calls>();
    }

    private class RefreshListTask extends Task<Object, Void> {

        public RefreshListTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
            java.util.Collection data = callsQuery.getResultList();
            for (Object entity : data) {
                entityManager.refresh(entity);
            }
            if (callsList != null) {
                callsList.clear();
            } else {
                callsList = new ArrayList<Calls>();
            }
            callsList.addAll(data);
            return true;
        }

        @Override
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
            filteredCalls.clear();
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        callsQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT c FROM Calls c");
        callsList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(callsQuery.getResultList());
        callsPanel = new javax.swing.JPanel();
        callsScrollPane = new javax.swing.JScrollPane();
        callsTable = new javax.swing.JTable();
        receiptStartDateLabel = new javax.swing.JLabel();
        receiptEndDateLabel = new javax.swing.JLabel();
        filterButton = new javax.swing.JButton();
        startDateField = new JXDatePicker();
        endDateField = new JXDatePicker();
        refreshButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        Application application = Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(CallsPanel.class);
        callsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("callsPanel.border.title"))); // NOI18N
        callsPanel.setName("callsPanel"); // NOI18N

        callsScrollPane.setName("callsSrollPane"); // NOI18N

        callsTable.setName("callsTable"); // NOI18N

        JTableBinding jTableBinding = SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ, callsList, callsTable);
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${caller}"));
        columnBinding.setColumnName("Caller");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${receiptDate}"));
        columnBinding.setColumnName("Receipt Date");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        callsScrollPane.setViewportView(callsTable);

        receiptStartDateLabel.setText(resourceMap.getString("receiptStartDateLabel.text")); // NOI18N
        receiptStartDateLabel.setName("receiptStartDateLabel"); // NOI18N

        receiptEndDateLabel.setText(resourceMap.getString("receiptEndDateLabel.text")); // NOI18N
        receiptEndDateLabel.setName("receiptEndDateLabel"); // NOI18N

        ActionMap actionMap = application.getContext().getActionMap(CallsPanel.class, this);
        filterButton.setAction(actionMap.get("filterAction")); // NOI18N
        filterButton.setText(resourceMap.getString("filterButton.text")); // NOI18N
        filterButton.setName("filterButton"); // NOI18N

        startDateField.setName("startDateField"); // NOI18N

        endDateField.setName("endDateField"); // NOI18N
        endDateField.setName("endDatePicker");

        refreshButton.setAction(actionMap.get("refreshAction")); // NOI18N
        refreshButton.setText(resourceMap.getString("refreshButton.text")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets 20 20"));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder("Calls Panel"));

        callsScrollPane.setViewportView(callsTable);
        Dimension dimension = new Dimension(100, receiptStartDateLabel.getHeight());
        startDateField.setPreferredSize(dimension);
        endDateField.setPreferredSize(dimension);

        this.add(receiptStartDateLabel, "");
        this.add(startDateField, "gapright 20");
        this.add(receiptEndDateLabel, "");
        this.add(endDateField, "");
        this.add(filterButton, "gapright 40, push, wrap");
        this.add(callsScrollPane, "spanx 5, grow, push, wrap");
        this.add(refreshButton, "skip 4, align right");

        bindingGroup.bind();
    }
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Calls> callsList;
    private javax.swing.JPanel callsPanel;
    private javax.persistence.Query callsQuery;
    private javax.swing.JScrollPane callsScrollPane;
    private javax.swing.JTable callsTable;
    private JXDatePicker endDateField;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JButton filterButton;
    private javax.swing.JLabel receiptEndDateLabel;
    private javax.swing.JLabel receiptStartDateLabel;
    private javax.swing.JButton refreshButton;
    private JXDatePicker startDateField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private List<Calls> filteredCalls;
    private static Logger log = LoggerHelper.getLogger();
}
