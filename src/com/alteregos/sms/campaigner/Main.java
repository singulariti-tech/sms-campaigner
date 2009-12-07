/*
 * Main.java
 */
package com.alteregos.sms.campaigner;

import com.alteregos.sms.campaigner.engine.Engine;
import com.alteregos.sms.campaigner.helpers.ProbeListener;
import com.alteregos.sms.campaigner.services.probe.ProbeResults;
import com.alteregos.sms.campaigner.services.probe.ProbeTask;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.views.MainView;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.data.dao.DatabaseCreator;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main class of the application.
 */
public class Main extends SingleFrameApplication {

    //<editor-fold defaultstate="collapsed" desc="Variable Declarations">
    private static Logger log = LoggerHelper.getLogger();
    private Configuration configuration;
    private Engine engine;
    private ProbeResults probeResults;
    private List<ProbeListener> probeListeners;
    private ApplicationContext springContext;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Lifecycle Methods">
    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        log.debug("Application startup");
        addExitListener(new CustomExitListener());
        springContext = new ClassPathXmlApplicationContext("classpath:campaignerContext-core.xml");
        //Check if db exists. If not create it
        DatabaseCreator databaseCreator = getBean("databaseInitializer");
        if (!databaseCreator.allTablesExist()) {
            log.debug("Tables not found. Initializing sqlite db");
            databaseCreator.createTables();
            databaseCreator.createIndices();
        }
        View mainView = new MainView(this);
        setLookAndFeel(getConfiguration().getLookAndFeel());
        show(mainView);
    }

    @Override
    protected void initialize(String[] arg0) {
        super.initialize(arg0);
        log.debug("Application initialization");
        //Verify License
        //verifyLicense();
        //Load Configuration
        loadConfiguration();
        //Initialize probe listeners
        probeListeners = new ArrayList<ProbeListener>();
    }

    @Override
    protected void ready() {
        super.ready();
        log.debug("Application ready");
        //Run probe on 
        runProbe();
        //Start services
        //if (results.isPortTestSuccessful()) {
        //start gateway & auto reply services
        //}
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        log.debug("Application shutdown");
        cleanUp();
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="Public convenience methods">
    /**
     * A convenient static getter for the application instance.
     * @return the instance of Main
     */
    public static Main getApplication() {
        return Main.getInstance(Main.class);
    }

    public void addProbeListener(ProbeListener listener) {
        log.debug("Probe Listener added");
        this.probeListeners.add(listener);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Engine getEngine() {
        return this.engine;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanId) {
        return (T) springContext.getBean(beanId);
    }

    public void setProbeResults(ProbeResults results) {
        log.debug("Probe results set");
        this.probeResults = results;

        String[] portNames = this.probeResults.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            this.probeResults.addPort(portNames[i]);
        }

        startServices();
    }

    public ProbeResults getProbeResults() {
        return probeResults;
    }

    public void setLookAndFeel(String className) {
        try {
            //Initialize Look & Feel
            log.debug("Setting look and feel: " + className);
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(getMainFrame());
        } catch (ClassNotFoundException ex) {
            log.debug("Look and feel class not found");
        } catch (InstantiationException ex) {
            log.debug("Could not instantiate look and feel");
        } catch (IllegalAccessException ex) {
            log.debug("Illegal access exception thrown...");
            log.debug(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            log.debug("Unsupported look and feel exception thrown");
        }
    }

    public void cleanUp() {
        log.debug("Clean up");
        //Stop engine
        if (engine != null) {
            log.debug("Stopping SMS engine");
            engine.stop();
        }
        updateLicense();
        saveConfiguration();
        getApplication().getContext().getTaskService().shutdownNow();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private convenience methods">
    private void runProbe() {
        log.debug("Starting probe");
        Task<Object, Void> probeTask = new ProbeTask(getApplication(), getConfiguration());
        //probeTask.setInputBlocker(new ProbeInputBlocker(probeTask, new BlockerDialog(getMainFrame(), true)));
        getApplication().getContext().getTaskService().execute(probeTask);
    }

    private void saveConfiguration() {
        //Save configuration
        log.debug("Saving configuration");
        OutputStream stream = null;
        try {
            stream = getContext().getLocalStorage().openOutputFile(Configuration.CONFIG_FILE_NAME);
        } catch (IOException ex) {
            log.error("IOException thrown");
            log.error(ex);
        }
        Configuration.save(stream);
    }

    private void updateLicense() {
        //LM update
        log.debug("Updating license");
        //licenseManager.updateLastUsedDate();
        //licenseManager.save();
    }

    private void verifyLicense() {
        log.debug("Verifying license");
    }

    private void loadConfiguration() {
        log.debug("Loading configuration");
        LocalStorage localStorage = getContext().getLocalStorage();
        InputStream stream = null;
        try {
            stream = localStorage.openInputFile(Configuration.CONFIG_FILE_NAME);
        } catch (IOException ex) {
            log.error("IOException when loading configuration");
            log.error(ex);
        }
        configuration = Configuration.load(stream);
    }

    private void startServices() {
        log.debug("Starting services");
        boolean isPortTestSuccessful = this.probeResults.isPortTestSuccessful();
        boolean isDbTestSuccessful = this.probeResults.isDbTestSuccessful();
        if (isPortTestSuccessful && isDbTestSuccessful) {
            log.debug("Starting SMS engine");
            engine = new Engine(this.probeResults.getModemSettings());
        }

        for (ProbeListener listener : this.probeListeners) {
            listener.probeEnded(this.probeResults);
        }
    }

    private class CustomExitListener implements Application.ExitListener {

        @Override
        public boolean canExit(EventObject e) {
            Object source = (e != null) ? e.getSource() : null;
            Component owner = (source instanceof Component) ? (Component) source : null;
            int option = JOptionPane.showConfirmDialog(owner, "Really Exit?");
            boolean exit = (option == JOptionPane.YES_OPTION);
            return exit;
        }

        @Override
        public void willExit(EventObject e) {
            // cleanup
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Main method">
    /**
     * Main method launching the application.
     * @param args 
     */
    public static void main(String[] args) {
        log.debug("Application launched");
        log.debug("JAVA.HOME: " + System.getProperty("java.home"));
        log.debug("Library path: " + System.getProperty("java.library.path"));
        try {
            launch(Main.class, args);
        } catch (Exception e) {
            log.debug("Catching exception in main");
            log.debug(e);
        }
    }
    //</editor-fold>
}
