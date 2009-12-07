package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.util.LookAndFeel;
import com.alteregos.sms.campaigner.util.StringUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The application's main frame.
 */
public class MainView extends FrameView {

    public MainView(SingleFrameApplication app) {
        super(app);
        initComponents();
        initApplicationResources();
        //Show Home
        showHome();
    }

    // <editor-fold defaultstate="collapsed" desc="Initialize Application Resources">
    private void initApplicationResources() {
        LookAndFeel configuredLook = LookAndFeel.getLookAndFeelFromClassName(Main.getApplication().getConfiguration().getLookAndFeel());
        ButtonGroup lookAndFeelGroup = new ButtonGroup();
        for (LookAndFeel lnf : LookAndFeel.values()) {
            String lnfName = StringUtils.toJavaVariableNameFormat(lnf.getLabel());
            javax.swing.JRadioButtonMenuItem menuItem = new javax.swing.JRadioButtonMenuItem();
            lookAndFeelGroup.add(menuItem);
            menuItem.setName(lnfName);
            lookMenuItem.add(menuItem);
            if (lnf.equals(configuredLook)) {
                menuItem.setSelected(true);
            }
            menuItem.setAction(new AbstractAction(lnf.getLabel()) {

                public static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    String className = LookAndFeel.getLookAndFeelFromLabel(e.getActionCommand()).getClassName();
                    Main.getApplication().setLookAndFeel(className);
                    Main.getApplication().getConfiguration().lookAndFeel(className);
                }
            });
        }

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = Main.getApplication().getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Main.getApplication().show(aboutBox);
    }

    @Action
    public void showHomeView() {
        showHome();
    }

    private void showHome() {
        HomePanel homePanel = new HomePanel();
        setComponent(homePanel);
        Main.getApplication().show(this);
    }

    @Action
    public void showNewSmsView() {
        newSmsView();
    }

    @Action
    public void showNewSmsViewToolbarAction() {
        newSmsView();
    }

    private void newSmsView() {
        SmsSenderPanel smsSender = new SmsSenderPanel();
        setComponent(smsSender);
        Main.getApplication().show(this);
    }

    @Action
    public void showNewAutoReplyRuleView() {
        newAutoReplyRuleView();
    }

    @Action
    public void showNewAutoReplyRuleViewToolbarAction() {
        newAutoReplyRuleView();
    }

    private void newAutoReplyRuleView() {
        RuleCreatorPanel ruleCreatorPanel = new RuleCreatorPanel();
        setComponent(ruleCreatorPanel);
        Main.getApplication().show(this);
    }

    @Action
    public void showAutoReplyRulesManagerView() {
        showAutoReplyRulesManager();
    }

    @Action
    public void showAutoReplyRulesManagerViewToolbarAction() {
        showAutoReplyRulesManager();
    }

    private void showAutoReplyRulesManager() {
        RulesManagerPanel autoReplyRulesManager = new RulesManagerPanel();
        setComponent(autoReplyRulesManager);
        Main.getApplication().show(this);
    }

    @Action
    public void showNewPhonebookEntryView() {
        newPhoneBookEntryView();
    }

    @Action
    public void showNewPhonebookEntryViewToolbarAction() {
        newPhoneBookEntryView();
    }

    private void newPhoneBookEntryView() {
        PhonebookEntryCreatorPanel phoneBookEntryCreator = new PhonebookEntryCreatorPanel();
        setComponent(phoneBookEntryCreator);
        Main.getApplication().show(this);
    }

    @Action
    public void showPhonebookManagerView() {
        phoneBookManagerView();
    }

    @Action
    public void showPhonebookManagerViewToolbarAction() {
        phoneBookManagerView();
    }

    private void phoneBookManagerView() {
        PhonebookManagerPanel phoneBookManager = new PhonebookManagerPanel();
        setComponent(phoneBookManager);
        Main.getApplication().show(this);
    }

    @Action
    public void showManageAutoReplyRulesView() {
        manageAutoReplyRulesView();
    }

    @Action
    public void showManageAutoReplyRulesToolbarView() {
        manageAutoReplyRulesView();
    }

    private void manageAutoReplyRulesView() {
        RulesManagerPanel autoReplyRulesManager = new RulesManagerPanel();
        setComponent(autoReplyRulesManager);
        Main.getApplication().show(this);
    }

    @Action
    public void showManagePhonebookView() {
        managePhonebookView();
    }

    @Action
    public void showManagePhonebookToolbarView() {
        managePhonebookView();
    }

    private void managePhonebookView() {
        newPhoneBookEntryView();
        //setComponent
    }

    @Action
    public void showInboxView() {
        inboxView();
    }

    @Action
    public void showInboxViewToolbarAction() {
        inboxView();
    }

    private void inboxView() {
        InboxPanel inboxPanel = new InboxPanel();
        setComponent(inboxPanel);
        Main.getApplication().show(this);
    }

    @Action
    public void showOutboxView() {
        outboxView();
    }

    @Action
    public void showOutboxViewToolbarAction() {
        outboxView();
    }

    private void outboxView() {
        OutboxPanel outboxPanel = new OutboxPanel();
        setComponent(outboxPanel);
        Main.getApplication().show(this);
    }

    @Action
    public void showReportsView() {
        reportsView();
    }

    @Action
    public void showReportsViewToolbarAction() {
        reportsView();
    }

    private void reportsView() {
        ReportGeneratorPanel reportGeneratorPanel = new ReportGeneratorPanel();
        setComponent(reportGeneratorPanel);
        Main.getApplication().show(this);
    }

    @Action
    public void showSettingsView() {
        settingsView();
    }

    @Action
    public void showSettingsViewToolbarAction() {
        settingsView();
    }

    private void settingsView() {
        if (settingsDialog == null) {
            JFrame mainFrame = Main.getApplication().getMainFrame();
            settingsDialog = new SettingsDialog(mainFrame, true);
            settingsDialog.setLocationRelativeTo(mainFrame);
        }
        Main.getApplication().show(settingsDialog);
    }

    @Action
    public void showServicesView() {
        servicesView();
    }

    @Action
    public void showServicesViewToolbarAction() {
        servicesView();
    }

    private void servicesView() {
        if (servicesDialog == null) {
            JFrame mainFrame = Main.getApplication().getMainFrame();
            servicesDialog = new ServicesDialog(mainFrame, true);
            servicesDialog.setLocationRelativeTo(servicesDialog);
        }
        Main.getApplication().show(servicesDialog);
    }

    @Action
    public void showUserManualView() {
        //show();
    }

    @Action
    public void showDndView() {
        showDnd();
    }

    @Action
    public void showDndViewToolbarAction() {
        showDnd();
    }

    private void showDnd() {
        DndPanel dndPanel = new DndPanel();
        setComponent(dndPanel);
        getApplication().show(this);
    }

    @Action
    public void showCallsReceivedView() {
        showCallsReceived();
    }

    @Action
    public void showCallsReceivedViewToolbarAction() {
        showCallsReceived();
    }

    private void showCallsReceived() {
        CallsPanel callsPanel = new CallsPanel();
        setComponent(callsPanel);
        getApplication().show(this);
    }

    @Action
    public void showNewGroupView() {
        showNewGroupWizard();
    }

    @Action
    public void showNewGroupViewToolbarAction() {
        showNewGroupWizard();
    }

    public void showNewGroupWizard() {
        //Added by V.Amar on 09-05-2008
        GroupCreatorPanel groupCreatorPanel = new GroupCreatorPanel();
        setComponent(groupCreatorPanel);
        getApplication().show(this);
    }

    @Action
    public void showManageGroupsView() {
        showManageGroups();
    }

    @Action
    public void showManageGroupsViewToolbarAction() {
        showManageGroups();
    }

    public void showManageGroups() {
        ManageGroupPanel manageGroupPanel = new ManageGroupPanel();
        setComponent(manageGroupPanel);
        getApplication().show(this);
    }

    //</editor-fold>
    @SuppressWarnings("unchecked")
    private void initComponents() {


        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newMenu = new javax.swing.JMenu();
        newSmsMenuItem = new javax.swing.JMenuItem();
        newMenuSeparator1 = new javax.swing.JSeparator();
        newAutoReplyRuleMenuItem = new javax.swing.JMenuItem();
        newMenuSeparator2 = new javax.swing.JSeparator();
        newPhonebookEntryMenuItem = new javax.swing.JMenuItem();
        newGroupMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        manageMenu = new javax.swing.JMenu();
        manageAutoReplyRulesMenuItem = new javax.swing.JMenuItem();
        manageMenuSeparator1 = new javax.swing.JSeparator();
        managePhoneBookMenuItem = new javax.swing.JMenuItem();
        manageGroupsMenuItem = new javax.swing.JMenuItem();
        smsMenu = new javax.swing.JMenu();
        inboxMenuItem = new javax.swing.JMenuItem();
        outboxMenuItem = new javax.swing.JMenuItem();
        callsReceivedMenuItem = new javax.swing.JMenuItem();
        smsMenuSeparator1 = new javax.swing.JSeparator();
        dndMenuItem = new javax.swing.JMenuItem();
        smsMenuSeparator2 = new javax.swing.JSeparator();
        reportsMenuItem = new javax.swing.JMenuItem();
        applicationMenu = new javax.swing.JMenu();
        servicesMenuItem = new javax.swing.JMenuItem();
        settingsMenuItem = new javax.swing.JMenuItem();
        lookMenuItem = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        userManualMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        toolBar = new javax.swing.JToolBar();
        homeToolBarButton = new javax.swing.JButton();
        toolBarSeparator1 = new javax.swing.JToolBar.Separator();
        newSmsToolBarButton = new javax.swing.JButton();
        newAutoReplyRuleToolbarButton = new javax.swing.JButton();
        newPhonebookEntryToolbarButton = new javax.swing.JButton();
        newGroupToolbarButton = new javax.swing.JButton();
        toolbarSeparator2 = new javax.swing.JToolBar.Separator();
        autoReplyRulesToolBarButton = new javax.swing.JButton();
        phoneBookToolBarButton = new javax.swing.JButton();
        groupsToolbarButton = new javax.swing.JButton();
        toolBarSeparator3 = new javax.swing.JToolBar.Separator();
        inboxToolBarButton = new javax.swing.JButton();
        outboxToolBarButton = new javax.swing.JButton();
        callsReceivedToolBarButton = new javax.swing.JButton();
        doNotDisturbToolbarButton = new javax.swing.JButton();
        toolBarSeparator4 = new javax.swing.JToolBar.Separator();
        reportsToolBarButton = new javax.swing.JButton();
        toolbarSeparator5 = new javax.swing.JToolBar.Separator();
        settingsToolBarButton = new javax.swing.JButton();
        servicesToolBarButton = new javax.swing.JButton();
        toolbarSeparator6 = new javax.swing.JToolBar.Separator();
        productLogoLabel = new javax.swing.JLabel();

        menuBar.setName("menuBar"); // NOI18N

        ResourceMap resourceMap = Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getResourceMap(MainView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newMenu.setIcon(resourceMap.getIcon("newMenu.icon")); // NOI18N
        newMenu.setText(resourceMap.getString("newMenu.text")); // NOI18N
        newMenu.setName("newMenu"); // NOI18N

        ActionMap actionMap = Application.getInstance(com.alteregos.sms.campaigner.Main.class).getContext().getActionMap(MainView.class, this);
        newSmsMenuItem.setAction(actionMap.get("showNewSmsView")); // NOI18N
        newSmsMenuItem.setName("newSmsMenuItem"); // NOI18N
        newMenu.add(newSmsMenuItem);

        newMenuSeparator1.setName("newMenuSeparator1"); // NOI18N
        newMenu.add(newMenuSeparator1);

        newAutoReplyRuleMenuItem.setAction(actionMap.get("showNewAutoReplyRuleView")); // NOI18N
        newAutoReplyRuleMenuItem.setName("newAutoReplyRuleMenuItem"); // NOI18N
        newMenu.add(newAutoReplyRuleMenuItem);

        newMenuSeparator2.setName("newMenuSeparator2"); // NOI18N
        newMenu.add(newMenuSeparator2);

        newPhonebookEntryMenuItem.setAction(actionMap.get("showNewPhonebookEntryView")); // NOI18N
        newPhonebookEntryMenuItem.setName("newPhonebookEntryMenuItem"); // NOI18N
        newMenu.add(newPhonebookEntryMenuItem);

        newGroupMenuItem.setAction(actionMap.get("showNewGroupView")); // NOI18N
        newGroupMenuItem.setName("newGroupMenuItem"); // NOI18N
        newMenu.add(newGroupMenuItem);

        fileMenu.add(newMenu);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        manageMenu.setText(resourceMap.getString("manageMenu.text")); // NOI18N
        manageMenu.setName("manageMenu"); // NOI18N

        manageAutoReplyRulesMenuItem.setAction(actionMap.get("showManageAutoReplyRulesView")); // NOI18N
        manageAutoReplyRulesMenuItem.setText(resourceMap.getString("manageAutoReplyRulesMenuItem.text")); // NOI18N
        manageAutoReplyRulesMenuItem.setName("manageAutoReplyRulesMenuItem"); // NOI18N
        manageMenu.add(manageAutoReplyRulesMenuItem);

        manageMenuSeparator1.setName("manageMenuSeparator1"); // NOI18N
        manageMenu.add(manageMenuSeparator1);

        managePhoneBookMenuItem.setAction(actionMap.get("showPhonebookManagerView")); // NOI18N
        managePhoneBookMenuItem.setText(resourceMap.getString("managePhoneBookMenuItem.text")); // NOI18N
        managePhoneBookMenuItem.setName("managePhoneBookMenuItem"); // NOI18N
        manageMenu.add(managePhoneBookMenuItem);

        manageGroupsMenuItem.setAction(actionMap.get("showManageGroupsView")); // NOI18N
        manageGroupsMenuItem.setName("manageGroupsMenuItem"); // NOI18N
        manageMenu.add(manageGroupsMenuItem);

        menuBar.add(manageMenu);

        smsMenu.setText(resourceMap.getString("smsMenu.text")); // NOI18N
        smsMenu.setName("smsMenu"); // NOI18N

        inboxMenuItem.setAction(actionMap.get("showInboxView")); // NOI18N
        inboxMenuItem.setText(resourceMap.getString("inboxMenuItem.text")); // NOI18N
        inboxMenuItem.setName("inboxMenuItem"); // NOI18N
        smsMenu.add(inboxMenuItem);

        outboxMenuItem.setAction(actionMap.get("showOutboxView")); // NOI18N
        outboxMenuItem.setText(resourceMap.getString("outboxMenuItem.text")); // NOI18N
        outboxMenuItem.setName("outboxMenuItem"); // NOI18N
        smsMenu.add(outboxMenuItem);

        callsReceivedMenuItem.setAction(actionMap.get("showCallsReceivedView")); // NOI18N
        callsReceivedMenuItem.setName("callsReceivedMenuItem"); // NOI18N
        smsMenu.add(callsReceivedMenuItem);

        smsMenuSeparator1.setName("smsMenuSeparator1"); // NOI18N
        smsMenu.add(smsMenuSeparator1);

        dndMenuItem.setAction(actionMap.get("showDndView")); // NOI18N
        dndMenuItem.setName("dndMenuItem"); // NOI18N
        smsMenu.add(dndMenuItem);

        smsMenuSeparator2.setName("smsMenuSeparator2"); // NOI18N
        smsMenu.add(smsMenuSeparator2);

        reportsMenuItem.setAction(actionMap.get("showReportsView")); // NOI18N
        reportsMenuItem.setText(resourceMap.getString("reportsMenuItem.text")); // NOI18N
        reportsMenuItem.setName("reportsMenuItem"); // NOI18N
        smsMenu.add(reportsMenuItem);

        menuBar.add(smsMenu);

        applicationMenu.setText(resourceMap.getString("applicationMenu.text")); // NOI18N
        applicationMenu.setName("applicationMenu"); // NOI18N

        servicesMenuItem.setAction(actionMap.get("showSettingsView")); // NOI18N
        servicesMenuItem.setName("servicesMenuItem"); // NOI18N
        applicationMenu.add(servicesMenuItem);

        settingsMenuItem.setAction(actionMap.get("showServicesView")); // NOI18N
        settingsMenuItem.setName("settingsMenuItem"); // NOI18N
        applicationMenu.add(settingsMenuItem);

        lookMenuItem.setText(resourceMap.getString("lookMenuItem.text")); // NOI18N
        lookMenuItem.setName("lookMenuItem"); // NOI18N
        applicationMenu.add(lookMenuItem);

        menuBar.add(applicationMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        userManualMenuItem.setAction(actionMap.get("showUserManualView")); // NOI18N
        userManualMenuItem.setText(resourceMap.getString("userManualMenuItem.text")); // NOI18N
        userManualMenuItem.setName("userManualMenuItem"); // NOI18N
        helpMenu.add(userManualMenuItem);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N        
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N        
        progressBar.setName("progressBar"); // NOI18N

        statusPanel.setLayout(new MigLayout("fill, insets 0 6 5 6", "[][]10[]", "[]"));
        statusPanel.add(statusMessageLabel, "push");
        statusPanel.add(progressBar);
        statusPanel.add(statusAnimationLabel);

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        homeToolBarButton.setAction(actionMap.get("showHomeView")); // NOI18N
        homeToolBarButton.setText(resourceMap.getString("homeToolBarButton.text")); // NOI18N
        homeToolBarButton.setToolTipText(resourceMap.getString("homeToolBarButton.toolTipText")); // NOI18N
        homeToolBarButton.setFocusable(false);
        homeToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        homeToolBarButton.setName("homeToolBarButton"); // NOI18N
        homeToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(homeToolBarButton);
        homeToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("homeToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        toolBarSeparator1.setName("toolBarSeparator1"); // NOI18N
        toolBar.add(toolBarSeparator1);

        newSmsToolBarButton.setAction(actionMap.get("showNewSmsViewToolbarAction")); // NOI18N
        newSmsToolBarButton.setFocusable(false);
        newSmsToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newSmsToolBarButton.setName("newSmsToolBarButton"); // NOI18N
        newSmsToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(newSmsToolBarButton);
        newSmsToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("newSmsToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        newAutoReplyRuleToolbarButton.setAction(actionMap.get("showNewAutoReplyRuleViewToolbarAction")); // NOI18N
        newAutoReplyRuleToolbarButton.setToolTipText(resourceMap.getString("newAutoReplyRuleToolbarButton.toolTipText")); // NOI18N
        newAutoReplyRuleToolbarButton.setFocusable(false);
        newAutoReplyRuleToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newAutoReplyRuleToolbarButton.setName("newAutoReplyRuleToolbarButton"); // NOI18N
        newAutoReplyRuleToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(newAutoReplyRuleToolbarButton);
        newAutoReplyRuleToolbarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("newAutoReplyRuleToolbarButton.AccessibleContext.accessibleName")); // NOI18N

        newPhonebookEntryToolbarButton.setAction(actionMap.get("showNewPhonebookEntryViewToolbarAction")); // NOI18N
        newPhonebookEntryToolbarButton.setFocusable(false);
        newPhonebookEntryToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newPhonebookEntryToolbarButton.setName("newPhonebookEntryToolbarButton"); // NOI18N
        newPhonebookEntryToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(newPhonebookEntryToolbarButton);
        newPhonebookEntryToolbarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("newPhonebookEntryToolbarButton.AccessibleContext.accessibleName")); // NOI18N

        newGroupToolbarButton.setAction(actionMap.get("showNewGroupViewToolbarAction")); // NOI18N
        newGroupToolbarButton.setFocusable(false);
        newGroupToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newGroupToolbarButton.setName("newGroupToolbarButton"); // NOI18N
        newGroupToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(newGroupToolbarButton);

        toolbarSeparator2.setName("toolbarSeparator2"); // NOI18N
        toolBar.add(toolbarSeparator2);

        autoReplyRulesToolBarButton.setAction(actionMap.get("showAutoReplyRulesManagerViewToolbarAction")); // NOI18N
        autoReplyRulesToolBarButton.setText(resourceMap.getString("autoReplyRulesToolBarButton.text")); // NOI18N
        autoReplyRulesToolBarButton.setFocusable(false);
        autoReplyRulesToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        autoReplyRulesToolBarButton.setName("autoReplyRulesToolBarButton"); // NOI18N
        autoReplyRulesToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(autoReplyRulesToolBarButton);
        autoReplyRulesToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("autoReplyRulesToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        phoneBookToolBarButton.setAction(actionMap.get("showPhonebookManagerViewToolbarAction")); // NOI18N
        phoneBookToolBarButton.setText(resourceMap.getString("phoneBookToolBarButton.text")); // NOI18N
        phoneBookToolBarButton.setFocusable(false);
        phoneBookToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        phoneBookToolBarButton.setName("phoneBookToolBarButton"); // NOI18N
        phoneBookToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(phoneBookToolBarButton);
        phoneBookToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("phoneBookToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        groupsToolbarButton.setAction(actionMap.get("showManageGroupsViewToolbarAction")); // NOI18N
        groupsToolbarButton.setFocusable(false);
        groupsToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        groupsToolbarButton.setName("groupsToolbarButton"); // NOI18N
        groupsToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(groupsToolbarButton);

        toolBarSeparator3.setName("toolBarSeparator3"); // NOI18N
        toolBar.add(toolBarSeparator3);

        inboxToolBarButton.setAction(actionMap.get("showInboxViewToolbarAction")); // NOI18N
        inboxToolBarButton.setText(resourceMap.getString("inboxToolBarButton.text")); // NOI18N
        inboxToolBarButton.setFocusable(false);
        inboxToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        inboxToolBarButton.setName("inboxToolBarButton"); // NOI18N
        inboxToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(inboxToolBarButton);
        inboxToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("inboxToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        outboxToolBarButton.setAction(actionMap.get("showOutboxViewToolbarAction")); // NOI18N
        outboxToolBarButton.setFocusable(false);
        outboxToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        outboxToolBarButton.setName("outboxToolBarButton"); // NOI18N
        outboxToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(outboxToolBarButton);
        outboxToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("outboxToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        callsReceivedToolBarButton.setAction(actionMap.get("showCallsReceivedViewToolbarAction")); // NOI18N
        callsReceivedToolBarButton.setFocusable(false);
        callsReceivedToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        callsReceivedToolBarButton.setName("callsReceivedToolBarButton"); // NOI18N
        callsReceivedToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(callsReceivedToolBarButton);

        doNotDisturbToolbarButton.setAction(actionMap.get("showDndViewToolbarAction")); // NOI18N
        doNotDisturbToolbarButton.setFocusable(false);
        doNotDisturbToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        doNotDisturbToolbarButton.setName("doNotDisturbToolbarButton"); // NOI18N
        doNotDisturbToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(doNotDisturbToolbarButton);

        toolBarSeparator4.setName("toolBarSeparator4"); // NOI18N
        toolBar.add(toolBarSeparator4);

        reportsToolBarButton.setAction(actionMap.get("showReportsViewToolbarAction")); // NOI18N
        reportsToolBarButton.setText(resourceMap.getString("reportsToolBarButton.text")); // NOI18N
        reportsToolBarButton.setFocusable(false);
        reportsToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        reportsToolBarButton.setName("reportsToolBarButton"); // NOI18N
        reportsToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(reportsToolBarButton);
        reportsToolBarButton.getAccessibleContext().setAccessibleName(resourceMap.getString("reportsToolBarButton.AccessibleContext.accessibleName")); // NOI18N

        toolbarSeparator5.setName("toolbarSeparator5"); // NOI18N
        toolBar.add(toolbarSeparator5);

        settingsToolBarButton.setAction(actionMap.get("showSettingsViewToolbarAction")); // NOI18N
        settingsToolBarButton.setFocusable(false);
        settingsToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        settingsToolBarButton.setName("settingsToolBarButton"); // NOI18N
        settingsToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(settingsToolBarButton);

        servicesToolBarButton.setAction(actionMap.get("showServicesViewToolbarAction")); // NOI18N
        servicesToolBarButton.setFocusable(false);
        servicesToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        servicesToolBarButton.setName("servicesToolBarButton"); // NOI18N
        servicesToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(servicesToolBarButton);

        toolbarSeparator6.setName("toolbarSeparator6"); // NOI18N
        toolBar.add(toolbarSeparator6);

        productLogoLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        productLogoLabel.setIcon(resourceMap.getIcon("productLogoLabel.icon")); // NOI18N
        productLogoLabel.setText(resourceMap.getString("productLogoLabel.text")); // NOI18N
        productLogoLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        productLogoLabel.setMaximumSize(new java.awt.Dimension(3840, 25));
        productLogoLabel.setName("productLogoLabel"); // NOI18N
        toolBar.add(productLogoLabel);

        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }
    private javax.swing.JMenu applicationMenu;
    private javax.swing.JButton autoReplyRulesToolBarButton;
    private javax.swing.JMenuItem callsReceivedMenuItem;
    private javax.swing.JButton callsReceivedToolBarButton;
    private javax.swing.JMenuItem dndMenuItem;
    private javax.swing.JButton doNotDisturbToolbarButton;
    private javax.swing.JButton groupsToolbarButton;
    private javax.swing.JButton homeToolBarButton;
    private javax.swing.JMenuItem inboxMenuItem;
    private javax.swing.JButton inboxToolBarButton;
    private javax.swing.JMenu lookMenuItem;
    private javax.swing.JMenuItem manageAutoReplyRulesMenuItem;
    private javax.swing.JMenuItem manageGroupsMenuItem;
    private javax.swing.JMenu manageMenu;
    private javax.swing.JSeparator manageMenuSeparator1;
    private javax.swing.JMenuItem managePhoneBookMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newAutoReplyRuleMenuItem;
    private javax.swing.JButton newAutoReplyRuleToolbarButton;
    private javax.swing.JMenuItem newGroupMenuItem;
    private javax.swing.JButton newGroupToolbarButton;
    private javax.swing.JMenu newMenu;
    private javax.swing.JSeparator newMenuSeparator1;
    private javax.swing.JSeparator newMenuSeparator2;
    private javax.swing.JMenuItem newPhonebookEntryMenuItem;
    private javax.swing.JButton newPhonebookEntryToolbarButton;
    private javax.swing.JMenuItem newSmsMenuItem;
    private javax.swing.JButton newSmsToolBarButton;
    private javax.swing.JMenuItem outboxMenuItem;
    private javax.swing.JButton outboxToolBarButton;
    private javax.swing.JButton phoneBookToolBarButton;
    private javax.swing.JLabel productLogoLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem reportsMenuItem;
    private javax.swing.JButton reportsToolBarButton;
    private javax.swing.JMenuItem servicesMenuItem;
    private javax.swing.JButton servicesToolBarButton;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JButton settingsToolBarButton;
    private javax.swing.JMenu smsMenu;
    private javax.swing.JSeparator smsMenuSeparator1;
    private javax.swing.JSeparator smsMenuSeparator2;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JToolBar.Separator toolBarSeparator1;
    private javax.swing.JToolBar.Separator toolBarSeparator3;
    private javax.swing.JToolBar.Separator toolBarSeparator4;
    private javax.swing.JToolBar.Separator toolbarSeparator2;
    private javax.swing.JToolBar.Separator toolbarSeparator5;
    private javax.swing.JToolBar.Separator toolbarSeparator6;
    private javax.swing.JMenuItem userManualMenuItem;
    private static Logger log = LoggerHelper.getLogger();
    private Timer messageTimer;
    private Timer busyIconTimer;
    private Icon idleIcon;
    private Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private JDialog settingsDialog;
    private JDialog servicesDialog;
}
