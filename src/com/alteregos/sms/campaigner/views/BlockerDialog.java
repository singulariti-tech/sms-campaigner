package com.alteregos.sms.campaigner.views;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author  John Emmanuel
 */
public class BlockerDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    /** Creates new form BlockerDialog
     * @param parent
     * @param modal
     */
    public BlockerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setMessage(String message) {
        statusLabel.setText(message);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        Application application = Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(BlockerDialog.class);
        statusLabel.setText(resourceMap.getString("statusLabel.text")); // NOI18N
        statusLabel.setName("statusLabel"); // NOI18N

        getContentPane().setLayout(new MigLayout("fill, inset 20", "", ""));
        getContentPane().add(statusLabel);

        pack();
    }
    private javax.swing.JLabel statusLabel;
}
