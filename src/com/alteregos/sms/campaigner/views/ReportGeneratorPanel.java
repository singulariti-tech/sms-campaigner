package com.alteregos.sms.campaigner.views;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author  John
 */
public class ReportGeneratorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form ReportGeneratorPanel */
    public ReportGeneratorPanel() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(ReportGeneratorPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("reportingManagerPanel.border.title"))); // NOI18N
        this.setName("reportingManagerPanel"); // NOI18N
        this.setLayout(new MigLayout("fill, insets panel"));
    }
}
