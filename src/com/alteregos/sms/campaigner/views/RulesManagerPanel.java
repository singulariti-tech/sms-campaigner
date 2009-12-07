package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.services.RuleService;
import com.alteregos.sms.campaigner.util.DateUtils;
import com.alteregos.sms.campaigner.views.helpers.SizeLimitedTextComponent;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author  John Emmanuel
 */
public class RulesManagerPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

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
        for (AutoReplyRule rule : ruleList) {
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
    public Task<Boolean, Void> refreshListAction() {
        return new RefreshListActionTask(Main.getApplication());
    }

    @Action
    public Task<ITaskResult, Void> saveAction() {
        return new SaveActionTask(org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class));
    }

    @Action
    public Task<ITaskResult, Void> deleteAction() {
        return new DeleteActionTask(org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class));
    }
    //</editor-fold>

    /**
     *
     */
    //<editor-fold defaultstate="collapsed" desc="Dependencies">
    private class RefreshListActionTask extends Task<Boolean, Void> {

        public RefreshListActionTask(Application arg0) {
            super(arg0);
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            java.util.Collection<AutoReplyRule> data = ruleService.getRules();
            if (ruleList == null) {
                ruleList = ObservableCollections.observableList(new ArrayList<AutoReplyRule>());
            }
            ruleList.clear();
            ruleList.addAll(data);
            return true;
        }

        @Override
        protected void succeeded(Boolean arg0) {
            super.succeeded(arg0);
        }
    }

    private class SaveActionTask extends org.jdesktop.application.Task<ITaskResult, Void> {

        SaveActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SaveActionTask fields, here.
            super(app);
        }

        @Override
        protected ITaskResult doInBackground() {
            ITaskResult result = null;
            try {
                int[] selected = rulesTable.getSelectedRows();
                List<AutoReplyRule> rules = new ArrayList<AutoReplyRule>();
                for (int i = 0; i < selected.length; i++) {
                    AutoReplyRule rule = ruleList.get(rulesTable.convertRowIndexToModel(selected[i]));
                    rule.setModifiedDate(new Date());
                    rules.add(rule);
                }
                int[] counts = ruleService.update(rules);
                //TODO Verify update counts
                result = new SuccessfulTaskResult();
            } catch (Exception rollbackException) {
                result = ExceptionParser.getError(rollbackException);
            }
            return result;
        }

        @Override
        protected void succeeded(ITaskResult taskResult) {
            super.succeeded(taskResult);
            ITaskResult result = taskResult;
            if (!result.isSuccessful()) {
                JOptionPane.showMessageDialog(Main.getApplication().getMainFrame(), "SET ERROR MESSAGE HERE");
            }
        }
    }

    private class DeleteActionTask extends org.jdesktop.application.Task<ITaskResult, Void> {

        DeleteActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to DeleteActionTask fields, here.
            super(app);
        }

        @Override
        protected ITaskResult doInBackground() {
            ITaskResult result = null;
            int[] selected = rulesTable.getSelectedRows();
            List<AutoReplyRule> toRemove = new ArrayList<AutoReplyRule>();
            for (int i = 0; i < selected.length; i++) {
                AutoReplyRule rule = ruleList.get(rulesTable.convertRowIndexToModel(selected[i]));
                toRemove.add(rule);
            }

            try {
                ruleService.delete(ruleList);
                result = new SuccessfulTaskResult();
            } catch (Exception rollbackException) {
                List<AutoReplyRule> toMerge = new ArrayList<AutoReplyRule>();
                for (AutoReplyRule rule : ruleList) {
                    //TODO rollback if delete fails
                }
                ruleList.clear();
                ruleList.addAll(toMerge);
                result = ExceptionParser.getError(rollbackException);
            }

            return result;
        }

        @Override
        protected void succeeded(ITaskResult taskResult) {
            super.succeeded(taskResult);
            ITaskResult result = taskResult;
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
        filteredRuleList = new ArrayList<AutoReplyRule>();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        ruleService = Main.getApplication().getBean("ruleService");

        ruleList = ObservableCollections.observableList(ruleService.getRules());
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
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N

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

        Dimension dateFieldsDimension = new Dimension(70, startDateLabel.getHeight());
        startDateField.setPreferredSize(dateFieldsDimension);
        endDateField.setPreferredSize(dateFieldsDimension);
        Dimension keywordFieldDimension = new Dimension(100, primaryKeywordLabel.getHeight());
        primaryKeywordLabel.setPreferredSize(keywordFieldDimension);
        secondaryKeywordLabel.setPreferredSize(keywordFieldDimension);

        this.setLayout(new MigLayout("fill, insets panel", "", "[][]20[min!][min!][min!][75!][min!]"));

        this.add(startDateLabel, "span, split 5");
        this.add(startDateField, "width 150!, gapright 20");
        this.add(endDateLabel);
        this.add(endDateField, "width 150!, gapright 20");
        this.add(filterButton, "wrap");
        this.add(rulesScrollPane, "spanx 5, grow, push, wrap");

        this.add(primaryKeywordLabel, "span, split 2");
        this.add(primaryKeywordField, "grow, wrap");
        this.add(secondaryKeywordLabel, "span, split 2");
        this.add(secondaryKeywordField, "grow, wrap");
        this.add(contentLabel, "wrap");
        this.add(contentScrollPane, "spanx 5, grow, push, wrap");
        this.add(contentLengthField, "spanx 2, split 2");
        this.add(enabledCheckbox, "push");
        this.add(saveButton, "spanx 3, split 3, right");
        this.add(deleteButton);
        this.add(refreshButton);

        bindingGroup.bind();
    }
    private RuleService ruleService;
    private javax.swing.JLabel contentLabel;
    private javax.swing.JTextField contentLengthField;
    private javax.swing.JScrollPane contentScrollPane;
    private javax.swing.JTextArea contentTextArea;
    private javax.swing.JButton deleteButton;
    private javax.swing.JCheckBox enabledCheckbox;
    private JXDatePicker endDateField;
    private JXDatePicker startDateField;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JButton filterButton;
    private javax.swing.JTextField primaryKeywordField;
    private javax.swing.JLabel primaryKeywordLabel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JScrollPane rulesScrollPane;
    private javax.swing.JTable rulesTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField secondaryKeywordField;
    private javax.swing.JLabel secondaryKeywordLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private List<AutoReplyRule> ruleList;
    private List<AutoReplyRule> filteredRuleList;
}
