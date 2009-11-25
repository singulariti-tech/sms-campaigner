package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.data.beans.Dnd;
import com.alteregos.sms.campaigner.util.DateUtils;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.views.helpers.DateColumnCellRenderer;
import java.awt.Dimension;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collections;
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
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class DndPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form DndPanel */
    public DndPanel() {
        initComponents();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task refreshListAction() {
        return new RefreshListTask(Application.getInstance(com.alteregos.sms.campaigner.Main.class));
    }

    @Action
    public void filterAction() {
        Date startDate = DateUtils.truncateDate(startDateField.getDate());
        Date endDate = DateUtils.getEndOfDay(DateUtils.truncateDate(endDateField.getDate()));
        boolean isEndDateAfterStartDate = endDate.after(startDate);
        if (!isEndDateAfterStartDate) {
            endDate = DateUtils.getEndOfDay(startDate);
            endDateField.setDate(endDate);
        }
        dndList.addAll(filteredDndList);
        filteredDndList.clear();
        for (Dnd dnd : dndList) {
            Date registeredDate = dnd.getRegisteredDate();
            boolean isRegisteredDateBeforeStartDate = registeredDate.before(startDate);
            boolean isRegisteredDateAfterEndDate = registeredDate.after(endDate);
            if (isRegisteredDateBeforeStartDate || isRegisteredDateAfterEndDate) {
                filteredDndList.add(dnd);
            }

        }
        dndList.removeAll(filteredDndList);
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private void initialize() {
        TableColumn registeredDateColumn = dndTable.getColumnModel().getColumn(1);
        registeredDateColumn.setCellRenderer(new DateColumnCellRenderer());
        filteredDndList = new ArrayList<Dnd>();
    }

    private class RefreshListTask extends Task {

        public RefreshListTask(Application app) {
            super(app);
        }

        @Override
        public Object doInBackground() {
            java.util.Collection data = dndQuery.getResultList();
            for (Object entity : data) {
                entityManager.refresh(entity);
            }
            if (dndList != null) {
                dndList.clear();
            } else {
                dndList = new ArrayList<Dnd>();
            }
            dndList.addAll(data);
            return true;
        }

        @Override
        public void succeeded(Object response) {
            dndTable.getSelectionModel().clearSelection();
        }
    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        dndQuery = Beans.isDesignTime() ? null : entityManager.createQuery("SELECT d FROM Dnd d");
        dndList = Beans.isDesignTime() ? Collections.emptyList() : ObservableCollections.observableList(dndQuery.getResultList());
        dndPanel = new javax.swing.JPanel();
        dndScrollPane = new javax.swing.JScrollPane();
        dndTable = new javax.swing.JTable();
        refreshButton = new javax.swing.JButton();
        startDateLabel = new javax.swing.JLabel();
        startDateField = new JXDatePicker();
        endDateLabel = new javax.swing.JLabel();
        endDateField = new JXDatePicker();
        filterButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        Application application = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(DndPanel.class);
        dndPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("dndPanel.border.title"))); // NOI18N
        dndPanel.setName("dndPanel"); // NOI18N

        dndScrollPane.setName("dndScrollPane"); // NOI18N

        dndTable.setName("dndTable"); // NOI18N

        JTableBinding jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ_WRITE, dndList, dndTable);
        JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${mobileNo}"));
        columnBinding.setColumnName("Mobile No");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registeredDate}"));
        columnBinding.setColumnName("Registered Date");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        dndScrollPane.setViewportView(dndTable);
        dndTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("dndTable.columnModel.title0")); // NOI18N
        dndTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("dndTable.columnModel.title1")); // NOI18N

        ActionMap actionMap = application.getContext().getActionMap(DndPanel.class, this);
        refreshButton.setAction(actionMap.get("refreshListAction")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        startDateLabel.setText(resourceMap.getString("startDateLabel.text")); // NOI18N
        startDateLabel.setName("startDateLabel"); // NOI18N

        startDateField.setName("startDateField"); // NOI18N

        endDateLabel.setText(resourceMap.getString("endDateLabel.text")); // NOI18N
        endDateLabel.setName("endDateLabel"); // NOI18N

        endDateField.setName("endDateField"); // NOI18N

        filterButton.setAction(actionMap.get("filterAction")); // NOI18N
        filterButton.setText(resourceMap.getString("filterButton.text")); // NOI18N
        filterButton.setName("filterButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets 20 20"));
        this.setBorder(javax.swing.BorderFactory.createTitledBorder("DND Registrations"));

        dndScrollPane.setViewportView(dndTable);
        Dimension dimension = new Dimension(100, startDateLabel.getHeight());
        startDateField.setPreferredSize(dimension);
        endDateField.setPreferredSize(dimension);

        this.add(startDateLabel, "");
        this.add(startDateField, "gapright 20");
        this.add(endDateLabel, "");
        this.add(endDateField, "gapright 40");
        this.add(filterButton, "push, wrap");
        this.add(dndScrollPane, "spanx 5, grow, push, wrap");
        this.add(refreshButton, "skip 4, align right");

        bindingGroup.bind();
    }
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Dnd> dndList;
    private javax.swing.JPanel dndPanel;
    private javax.persistence.Query dndQuery;
    private javax.swing.JScrollPane dndScrollPane;
    private javax.swing.JTable dndTable;
    private JXDatePicker endDateField;
    private javax.swing.JLabel endDateLabel;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JButton filterButton;
    private javax.swing.JButton refreshButton;
    private JXDatePicker startDateField;
    private javax.swing.JLabel startDateLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private List<Dnd> filteredDndList;
    private static Logger log = LoggerHelper.getLogger();
}
