package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.data.beans.Phonebook;
import com.alteregos.sms.campaigner.data.beans.Smsgroup;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jdesktop.application.Action;

/**
 *
 * @author  John Emmanuel
 */
public class SmsCreatorGroupsDialog extends javax.swing.JDialog {

    /** Creates new form SmsCreatorGroupsDialog */
    public SmsCreatorGroupsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public SmsCreatorGroupsDialog(Frame owner, boolean modal, SmsSenderPanel panel, List<String> recepients) {
        this(owner, modal);
        this.panel = panel;
        this.recepients = recepients;
        filterGroups(smsgroupList);
    }

    private List<String> filterGroups(List<Smsgroup> groupsList) {
        List<Smsgroup> filteredGroups = new ArrayList<Smsgroup>();
        for (Smsgroup group : groupsList) {
            boolean missed = false;
            Collection<Phonebook> phoneBookCollection = group.getPhoneBookCollection();
            Iterator<Phonebook> iterator = phoneBookCollection.iterator();
            while (iterator.hasNext()) {
                Phonebook contact = iterator.next();
                String number = contact.getMobileNo();
                if (!this.recepients.contains(number)) {
                    missed = true;
                }
            }

            if (!missed) {
                filteredGroups.add(group);
            }
        }

        this.smsgroupList.removeAll(filteredGroups);
        return this.recepients;
    }

    private List<String> filterRecepients(List<Smsgroup> groupsList) {
        List<String> finalRecepients = new ArrayList<String>();
        for (Smsgroup group : groupsList) {
            Collection<Phonebook> phoneBookCollection = group.getPhoneBookCollection();
            Iterator<Phonebook> iterator = phoneBookCollection.iterator();
            while (iterator.hasNext()) {
                Phonebook book = iterator.next();
                String number = book.getMobileNo();
                if (!this.recepients.contains(number)) {
                    finalRecepients.add(number);
                }
            }
        }

        finalRecepients.addAll(this.recepients);
        return finalRecepients;
    }

    @Action
    public void addRecepientsAction() {
        int[] selectedRows = groupsTable.getSelectedRows();
        List<Smsgroup> selectedGroupsList = new ArrayList<Smsgroup>();
        for (int index = 0; index < selectedRows.length; index++) {
            Smsgroup group = smsgroupList.get(groupsTable.convertRowIndexToModel(selectedRows[index]));
            selectedGroupsList.add(group);
        }

        List<String> finalRecepientList = filterRecepients(selectedGroupsList);
        this.panel.setRecepients(finalRecepientList);
        this.dispose();
    }

    @Action
    public void closeAction() {
        this.dispose();
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        smsgroupQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT s FROM Smsgroup s");
        smsgroupList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(smsgroupQuery.getResultList());
        borderContainer = new javax.swing.JPanel();
        groupsScrollPane = new javax.swing.JScrollPane();
        groupsTable = new javax.swing.JTable();
        addRecepientsButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(SmsCreatorGroupsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(320, 240));
        setName("Form"); // NOI18N
        setResizable(false);

        borderContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("borderContainer.border.title"))); // NOI18N
        borderContainer.setName("borderContainer"); // NOI18N

        groupsScrollPane.setName("groupsScrollPane"); // NOI18N

        groupsTable.setName("groupsTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, smsgroupList, groupsTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        groupsScrollPane.setViewportView(groupsTable);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(SmsCreatorGroupsDialog.class, this);
        addRecepientsButton.setAction(actionMap.get("addRecepientsAction")); // NOI18N
        addRecepientsButton.setText(resourceMap.getString("addRecepientsButton.text")); // NOI18N
        addRecepientsButton.setName("addRecepientsButton"); // NOI18N

        closeButton.setAction(actionMap.get("closeAction")); // NOI18N
        closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        javax.swing.GroupLayout borderContainerLayout = new javax.swing.GroupLayout(borderContainer);
        borderContainer.setLayout(borderContainerLayout);
        borderContainerLayout.setHorizontalGroup(
                borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(borderContainerLayout.createSequentialGroup().addContainerGap().addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(groupsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, borderContainerLayout.createSequentialGroup().addComponent(addRecepientsButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(closeButton))).addContainerGap()));
        borderContainerLayout.setVerticalGroup(
                borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(borderContainerLayout.createSequentialGroup().addContainerGap().addComponent(groupsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(borderContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(closeButton).addComponent(addRecepientsButton)).addGap(14, 14, 14)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(borderContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(borderContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));

        bindingGroup.bind();

        pack();
    }

    private javax.swing.JButton addRecepientsButton;
    private javax.swing.JPanel borderContainer;
    private javax.swing.JButton closeButton;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JScrollPane groupsScrollPane;
    private javax.swing.JTable groupsTable;
    private java.util.List<com.alteregos.sms.campaigner.data.beans.Smsgroup> smsgroupList;
    private javax.persistence.Query smsgroupQuery;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    
    private SmsSenderPanel panel;
    private List<String> recepients;
}
