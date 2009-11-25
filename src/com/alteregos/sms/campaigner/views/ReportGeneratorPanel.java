package com.alteregos.sms.campaigner.views;

/**
 *
 * @author  John
 */
public class ReportGeneratorPanel extends javax.swing.JPanel {

    /** Creates new form ReportGeneratorPanel */
    public ReportGeneratorPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        reportingManagerPanel = new javax.swing.JPanel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(ReportGeneratorPanel.class);
        reportingManagerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("reportingManagerPanel.border.title"))); // NOI18N
        reportingManagerPanel.setName("reportingManagerPanel"); // NOI18N

        javax.swing.GroupLayout reportingManagerPanelLayout = new javax.swing.GroupLayout(reportingManagerPanel);
        reportingManagerPanel.setLayout(reportingManagerPanelLayout);
        reportingManagerPanelLayout.setHorizontalGroup(
                reportingManagerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 364, Short.MAX_VALUE));
        reportingManagerPanelLayout.setVerticalGroup(
                reportingManagerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 248, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(reportingManagerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(reportingManagerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
    }

    private javax.swing.JPanel reportingManagerPanel;    
}
