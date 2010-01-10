package com.alteregos.sms.campaigner.views;

import java.awt.Container;
import javax.swing.ActionMap;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

public class AboutBox extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    //Components
    private javax.swing.JButton closeButton;

    public AboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
    }

    @Action
    public void closeAboutBox() {
        setVisible(false);
    }

    private void initComponents() {

        closeButton = new javax.swing.JButton();
        JLabel appTitleLabel = new JLabel();
        JLabel versionLabel = new JLabel();
        JLabel appVersionLabel = new JLabel();
        JLabel vendorLabel = new JLabel();
        JLabel appVendorLabel = new JLabel();
        JLabel homepageLabel = new JLabel();
        JLabel appHomepageLabel = new JLabel();
        JLabel appDescLabel = new JLabel();
        JLabel imageLabel = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Application application = Application.getInstance(com.alteregos.sms.campaigner.Main.class);
        ResourceMap resourceMap = application.getContext().getResourceMap(AboutBox.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setModal(true);
        setName("aboutBox"); // NOI18N
        setResizable(false);

        ActionMap actionMap = application.getContext().getActionMap(AboutBox.class, this);
        closeButton.setAction(actionMap.get("closeAboutBox")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() |
                java.awt.Font.BOLD, appTitleLabel.getFont().getSize() + 4));
        appTitleLabel.setText(resourceMap.getString("Application.title")); // NOI18N
        appTitleLabel.setName("appTitleLabel"); // NOI18N

        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel.setText(resourceMap.getString("versionLabel.text")); // NOI18N
        versionLabel.setName("versionLabel"); // NOI18N

        appVersionLabel.setText(resourceMap.getString("Application.version")); // NOI18N
        appVersionLabel.setName("appVersionLabel"); // NOI18N

        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text")); // NOI18N
        vendorLabel.setName("vendorLabel"); // NOI18N

        appVendorLabel.setText(resourceMap.getString("Application.vendor")); // NOI18N
        appVendorLabel.setName("appVendorLabel"); // NOI18N

        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText(resourceMap.getString("homepageLabel.text")); // NOI18N
        homepageLabel.setName("homepageLabel"); // NOI18N

        appHomepageLabel.setText(resourceMap.getString("Application.homepage")); // NOI18N
        appHomepageLabel.setName("appHomepageLabel"); // NOI18N

        appDescLabel.setText(resourceMap.getString("appDescLabel.text")); // NOI18N
        appDescLabel.setName("appDescLabel"); // NOI18N

        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon")); // NOI18N
        imageLabel.setName("imageLabel"); // NOI18N

        getRootPane().setDefaultButton(closeButton);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("fill, inset 20", "[]20[][]", ""));

        contentPane.add(imageLabel, "spany, center");
        contentPane.add(appTitleLabel, "spanx 2, wrap");
        contentPane.add(appDescLabel, "spanx 2, wrap");
        contentPane.add(versionLabel);
        contentPane.add(appVersionLabel, "wrap");
        contentPane.add(vendorLabel);
        contentPane.add(appVendorLabel, "wrap");
        contentPane.add(homepageLabel);
        contentPane.add(appHomepageLabel, "wrap");
        contentPane.add(closeButton, "skip 1, align right, push");

        pack();
    }    
}
