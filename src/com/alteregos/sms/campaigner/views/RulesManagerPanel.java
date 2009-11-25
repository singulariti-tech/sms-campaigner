package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.beans.Rule;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.util.DateUtils;
import com.alteregos.sms.campaigner.views.helpers.SizeLimitedTextComponent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.RollbackException;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class RulesManagerPanel extends javax.swing.JPanel {

    /** Creates new form RulesManagerPanel */
    public RulesManagerPanel() {
        initComponents();
        addListener();
        initialize();
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void filterListAction() {
        Date startDate = DateUtils.truncateDate(startDateField.getDate());
        Date endDate = DateUtils.getEndOfDay(DateUtils.truncateDate(endDateField.getDate()));
        boolean isEndDateAfterStartDate = endDate.after(startDate);
        if (!isEndDateAfterStartDate) {
            endDate = DateUtils.getEndOfDay(startDate);
            endDateField.setDate(endDate);
        }

        ruleList.addAll(filteredRuleList);
        filteredRuleList.clear();
        for (Rule rule : ruleList) {
            Date createdDate = rule.getCreatedDate();
            boolean isRegisteredDateBeforeStartDate = createdDate.before(startDate);
            boolean isRegisteredDateAfterEndDate = createdDate.after(endDate);
            if (isRegisteredDateBeforeStartDate || isRegisteredDateAfterEndDate) {
                filteredRuleList.add(rule);
            }
        }

        ruleList.removeAll(filteredRuleList);
    }

    @Action
    public Task refreshListAction() {
        return new RefreshListActionTask(Main.getApplication());
    }

    @Action
    public Task saveAction() {
        return new SaveActionTask(org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class));
    }

    @Action
    public Task deleteAction() {
        return new DeleteActionTask(org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class));
    }
    //</editor-fold>

    /**
     * 
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private class RefreshListActionTask extends Task<Object, Void> {

        public RefreshListActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Object doInBackground() throws Exception {
            entityManager.getTransaction().begin();
            entityManager.getTransaction().rollback();
            java.util.Collection data = ruleQuery.getResultList();
            for (Object entity : data) {
                entityManager.refresh(entity);
            }
            if (ruleList != null) {
                ruleList.clear();
            } else {
                ruleList = new ArrayList<Rule>();
            }
            ruleList.addAll(data);
            return true;
        }

        @Override
        protected void succeeded(Object arg0) {
            super.succeeded(arg0);
        }
    }

    private class SaveActionTask extends org.jdesktop.application.Task<Object, Void> {

        SaveActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SaveActionTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            ITaskResult result = null;
            try {
                entityManager.getTransaction().begin();
                int[] selected = rulesTable.getSelectedRows();
                for (int i = 0; i < selected.length; i++) {
                    Rule rule = ruleList.get(rulesTable.convertRowIndexToModel(selected[i]));
                    rule.setModifiedDate(new Date());
                }
                entityManager.getTransaction().commit();
                result = new SuccessfulTaskResult();
            } catch (RollbackException rollbackException) {
                result = ExceptionParser.getError(rollbackException);
            }
            return result;
        }

        @Override
        protected void succeeded(Object taskResult) {
            super.succeeded(taskResult);
            ITaskResult result = (ITaskResult) taskResult;
            if (!result.isSuccessful()) {
                JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), "SET ERROR MESSAGE HERE");
            }
        }
    }

    private class DeleteActionTask extends org.jdesktop.application.Task<Object, Void> {

        DeleteActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to DeleteActionTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            ITaskResult result = null;
            int[] selected = rulesTable.getSelectedRows();
            List<Rule> toRemove = new ArrayList<Rule>();
            for (int i = 0; i < selected.length; i++) {
                Rule rule = ruleList.get(rulesTable.convertRowIndexToModel(selected[i]));
                toRemove.add(rule);
                entityManager.remove(rule);
            }

            try {
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
                result = new SuccessfulTaskResult();
            } catch (RollbackException rollbackException) {
                List<Rule> toMerge = new ArrayList<Rule>();
                for (Rule rule : ruleList) {
                    toMerge.add(entityManager.merge(rule));
                }
                ruleList.clear();
                ruleList.addAll(toMerge);
                result = ExceptionParser.getError(rollbackException);
            }

            return result;
        }

        @Override
        protected void succeeded(Object taskResult) {
            super.succeeded(taskResult);
            ITaskResult result = (ITaskResult) taskResult;
            if (!result.isSuccessful()) {
                JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), result.getResultMessage().getLabel());
            } else {
                rulesTable.getSelectionModel().clearSelection();
                rulesTable.clearSelection();
            }
        }
    }

    private void addListener() {
        JTextComponent messageComponent = contentTextArea;
        int messageMaxLength = Integer.parseInt(contentLengthField.getText());
        messageComponent.setDocument(new SizeLimitedTextComponent(messageMaxLength, contentLengthField));
    }

    private void initialize() {
        filteredRuleList = new ArrayList<Rule>();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        ruleQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT r FROM Rule r");
        ruleList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(ruleQuery.getResultList());
        borderContainer = new javax.swing.JPanel();
        startDateLabel = new javax.swing.JLabel();
        startDateField = new JXDatePicker();
        endDateLabel = new javax.swing.JLabel();
        endDateField = new JXDatePicker();
        filterButton = new javax.swing.JButton();
        rulesScrollPane = new javax.swing.JScrollPane();
        rulesTable = new javax.swing.JTable();
        primaryKeywordLabel = new javax.swing.JLabel();
        primaryKeywordField = new javax.swing.JTextField();
        secondaryKeywordLabel = new javax.swing.JLabel();
        secondaryKeywordField = new javax.swing.JTextField();
        contentLabel = new javax.swing.JLabel();
        contentScrollPane = new javax.swing.JScrollPane();
        contentTextArea = new javax.swing.JTextArea();
        contentLengthField = new javax.swing.JTextField();
        enabledCheckbox = new javax.swing.JCheckBox();
        refreshButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(RulesManagerPanel.class);
        borderContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N
        borderContainer.setName("borderContainer"); // NOI18N

        startDateLabel.setText(resourceMap.getString("startDateLabel.text")); // NOI18N
        startDateLabel.setName("startDateLabel"); // NOI18N

        startDateField.setName("startDateField"); // NOI18N

        endDateLabel.setText(resourceMap.getString("endDateLabel.text")); // NOI18N
        endDateLabel.setName("endDateLabel"); // NOI18N

        endDateField.setName("endDateField"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(RulesManagerPanel.class, this);
        filterButton.setAction(actionMap.get("filterListAction")); // NOI18N
        filterButton.setName("filterButton"); // NOI18N

        rulesScrollPane.setName("rulesScrollPane"); // NOI18N

        rulesTable.setName("rulesTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, ruleList, rulesTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${primaryKeyword}"));
        columnBinding.setColumnName("Primary Keyword");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${secondaryKeyword}"));
        columnBinding.setColumnName("Secondary Keyword");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${content}"));
        columnBinding.setColumnName("Content");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${enabled}"));
        columnBinding.setColumnName("Enabled");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${createdDate}"));
        columnBinding.setColumnName("Created Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${modifiedDate}"));
        columnBinding.setColumnName("Modified Date");
        columnBinding.setColumnClass(java.util.Date.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        rulesScrollPane.setViewportView(rulesTable);

        primaryKeywordLabel.setText(resourceMap.getString("primaryKeywordLabel.text")); // NOI18N
        primaryKeywordLabel.setName("primaryKeywordLabel"); // NOI18N

        primaryKeywordField.setName("primaryKeywordField"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.primaryKeyword}"), primaryKeywordField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), primaryKeywordField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        secondaryKeywordLabel.setText(resourceMap.getString("secondaryKeywordLabel.text")); // NOI18N
        secondaryKeywordLabel.setName("secondaryKeywordLabel"); // NOI18N

        secondaryKeywordField.setName("secondaryKeywordField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.secondaryKeyword}"), secondaryKeywordField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), secondaryKeywordField, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        contentLabel.setText(resourceMap.getString("contentLabel.text")); // NOI18N
        contentLabel.setName("contentLabel"); // NOI18N

        contentScrollPane.setName("contentScrollPane"); // NOI18N

        contentTextArea.setColumns(20);
        contentTextArea.setFont(resourceMap.getFont("contentTextArea.font")); // NOI18N
        contentTextArea.setRows(5);
        contentTextArea.setName("contentTextArea"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.content}"), contentTextArea, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceUnreadableValue(null);
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), contentTextArea, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        contentScrollPane.setViewportView(contentTextArea);

        contentLengthField.setColumns(3);
        contentLengthField.setEditable(false);
        contentLengthField.setText(resourceMap.getString("contentLengthField.text")); // NOI18N
        contentLengthField.setName("contentLengthField"); // NOI18N

        enabledCheckbox.setText(resourceMap.getString("enabledCheckbox.text")); // NOI18N
        enabledCheckbox.setName("enabledCheckbox"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.enabled}"), enabledCheckbox, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), enabledCheckbox, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        refreshButton.setAction(actionMap.get("refreshListAction")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        deleteButton.setAction(actionMap.get("deleteAction")); // NOI18N
        deleteButton.setText(resourceMap.getString("deleteButton.text")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), deleteButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        saveButton.setAction(actionMap.get("saveAction")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rulesTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement != null}"), saveButton, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout borderContainerLayout = new javax.swing.GroupLayout(borderContainer);
        borderContainer.setLayout(borderContainerLayout);
        borderContainerLayout.setHorizontalGroup(
                borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(borderContainerLayout.createSequentialGroup().addContainerGap().addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(rulesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE).addGroup(borderContainerLayout.createSequentialGroup().addComponent(startDateLabel).addGap(6, 6, 6).addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(endDateLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(filterButton)).addComponent(contentLabel).addComponent(contentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE).addGroup(borderContainerLayout.createSequentialGroup().addComponent(contentLengthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(enabledCheckbox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 303, Short.MAX_VALUE).addComponent(saveButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(deleteButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(refreshButton)).addGroup(borderContainerLayout.createSequentialGroup().addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(secondaryKeywordLabel).addComponent(primaryKeywordLabel)).addGap(12, 12, 12).addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(secondaryKeywordField, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE).addComponent(primaryKeywordField, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)))).addContainerGap()));
        borderContainerLayout.setVerticalGroup(
                borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(borderContainerLayout.createSequentialGroup().addGap(11, 11, 11).addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(startDateLabel).addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(endDateLabel).addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(filterButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rulesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(primaryKeywordLabel).addComponent(primaryKeywordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(secondaryKeywordLabel).addComponent(secondaryKeywordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(contentLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(contentScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(contentLengthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(enabledCheckbox).addComponent(refreshButton).addComponent(deleteButton).addComponent(saveButton)).addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(borderContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(borderContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));

        bindingGroup.bind();
    }
    private javax.swing.JPanel borderContainer;
    private javax.swing.JLabel contentLabel;
    private javax.swing.JTextField contentLengthField;
    private javax.swing.JScrollPane contentScrollPane;
    private javax.swing.JTextArea contentTextArea;
    private javax.swing.JButton deleteButton;
    private javax.swing.JCheckBox enabledCheckbox;
    private JXDatePicker endDateField;
    private javax.swing.JLabel endDateLabel;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JButton filterButton;
    private javax.swing.JTextField primaryKeywordField;
    private javax.swing.JLabel primaryKeywordLabel;
    private javax.swing.JButton refreshButton;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Rule> ruleList;
    private javax.persistence.Query ruleQuery;
    private javax.swing.JScrollPane rulesScrollPane;
    private javax.swing.JTable rulesTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField secondaryKeywordField;
    private javax.swing.JLabel secondaryKeywordLabel;
    private JXDatePicker startDateField;
    private javax.swing.JLabel startDateLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private List<Rule> filteredRuleList;
}
