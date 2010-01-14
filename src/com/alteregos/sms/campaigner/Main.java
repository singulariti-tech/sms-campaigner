/*
 * Main.java
 */
package com.alteregos.sms.campaigner;

import com.alteregos.sms.campaigner.engine.Engine;
import com.alteregos.sms.campaigner.helpers.ProbeListener;
import com.alteregos.sms.campaigner.services.probe.ProbeResults;
import com.alteregos.sms.campaigner.services.probe.ProbeTask;
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
import org.jdesktop.application.Application;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.Task;
import org.jdesktop.application.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main class of the application.
 */
public class Main extends SingleFrameApplication {

    //<editor-fold defaultstate="collapsed" desc="Variable Declarations">
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
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
        LOGGER.debug(">> startup()");
        addExitListener(new CustomExitListener());
        springContext = new ClassPathXmlApplicationContext("classpath:campaignerContext-core.xml");
        //Check if db exists. If not create it
        DatabaseCreator databaseCreator = getBean("databaseInitializer");
        if (!databaseCreator.allTablesExist()) {
            LOGGER.debug("-- Database tables not found. Initializing sqlite db");
            databaseCreator.createTables();
            databaseCreator.createIndices();
        }
        View mainView = new MainView(this);
        setLookAndFeel(getConfiguration().getLookAndFeel());
        show(mainView);
        LOGGER.debug("<< startup()");
    }

    @Override
    protected void initialize(String[] arg0) {
        super.initialize(arg0);
        LOGGER.debug(">> initialize()");
        //Verify License
        //verifyLicense();
        //Load Configuration
        loadConfiguration();
        //Initialize probe listeners
        probeListeners = new ArrayList<ProbeListener>();
        LOGGER.debug("<< initialize()");
    }

    @Override
    protected void ready() {
        super.ready();
        LOGGER.debug(">> ready()");
        //Run probe on 
        runProbe();
        //Start services
        //if (results.isPortTestSuccessful()) {
        //start gateway & auto reply services
        //}
        LOGGER.debug("<< ready()");
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        LOGGER.debug(">> shutdown()");
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
        LOGGER.debug(">> addProbeListener()");
        this.probeListeners.add(listener);
        LOGGER.debug("<< addProbeListener()");
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
        LOGGER.debug(">> setProbeResults()");
        this.probeResults = results;

        String[] portNames = this.probeResults.getPortNames();
        for (int i = 0; i < portNames.length; i++) {
            LOGGER.debug("-- adding available port: {}", portNames[i]);
            this.probeResults.addPort(portNames[i]);
        }

        startServices();
        LOGGER.debug("<< setProbeResults()");
    }

    public ProbeResults getProbeResults() {
        return probeResults;
    }

    public void setLookAndFeel(String className) {
        LOGGER.debug(">> setLookAndFeel()");
        try {
            //Initialize Look & Feel
            LOGGER.debug("-- classname {} ", className);
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(getMainFrame());
        } catch (ClassNotFoundException ex) {
            LOGGER.debug("-- Look and feel class not found");
        } catch (InstantiationException ex) {
            LOGGER.debug("-- Could not instantiate look and feel");
        } catch (IllegalAccessException ex) {
            LOGGER.debug("-- Illegal access exception thrown: {}", ex);
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.debug("-- Unsupported look and feel exception thrown");
        }
        LOGGER.debug("<< setLookAndFeel()");
    }

    public void cleanUp() {
        LOGGER.debug(">> cleanUp()");
        //Stop engine
        if (engine != null) {
            LOGGER.debug("-- calling engine.stop()");
            engine.stop();
        }
        updateLicense();
        saveConfiguration();
        getApplication().getContext().getTaskService().shutdownNow();
        LOGGER.debug("<< cleanUp()");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private convenience methods">
    private void runProbe() {
        LOGGER.debug(">> runProbe()");
        LOGGER.info("-- Probing for devices");
        Task<Object, Void> probeTask = new ProbeTask(getApplication(), getConfiguration());
        //probeTask.setInputBlocker(new ProbeInputBlocker(probeTask, new BlockerDialog(getMainFrame(), true)));
        getApplication().getContext().getTaskService().execute(probeTask);
        LOGGER.debug("<< runProbe()");
    }

    private void saveConfiguration() {
        //Save configuration
        LOGGER.debug(">> saveConfiguration()");
        OutputStream stream = null;
        try {
            stream = getContext().getLocalStorage().openOutputFile(Configuration.CONFIG_FILE_NAME);
        } catch (IOException ex) {
            LOGGER.error("-- IOException thrown: {}", ex);
        }
        Configuration.save(stream);
        LOGGER.debug("<< saveConfiguration()");
    }

    private void updateLicense() {
        //LM update
        LOGGER.debug(">> updateLicense()");
        //licenseManager.updateLastUsedDate();
        //licenseManager.save();
        LOGGER.debug("<< updateLicense()");
    }

    private void verifyLicense() {
        LOGGER.debug(">> verifyLicense()");
    }

    private void loadConfiguration() {
        LOGGER.debug(">> loadConfiguration()");
        LocalStorage localStorage = getContext().getLocalStorage();
        InputStream stream = null;
        try {
            stream = localStorage.openInputFile(Configuration.CONFIG_FILE_NAME);
        } catch (IOException ex) {
            LOGGER.error("-- IOException when loading configuration: {}", ex);
        }
        configuration = Configuration.load(stream);
        LOGGER.debug("<< loadConfiguration()");
    }

    private void startServices() {
        LOGGER.debug(">> startServices()");
        boolean isPortTestSuccessful = this.probeResults.isPortTestSuccessful();
        boolean isDbTestSuccessful = this.probeResults.isDbTestSuccessful();
        if (isPortTestSuccessful && isDbTestSuccessful) {
            LOGGER.debug("-- creating SMS engine");
            engine = new Engine(this.probeResults.getModemSettings());
        }

        for (ProbeListener listener : this.probeListeners) {
            listener.probeEnded(this.probeResults);
        }
        LOGGER.debug("<< startServices()");
    }

    private class CustomExitListener implements Application.ExitListener {

        public CustomExitListener() {
            LOGGER.debug("** CustomeExitListener()");
        }

        @Override
        public boolean canExit(EventObject e) {
            LOGGER.debug(">> canExit()");
            Object source = (e != null) ? e.getSource() : null;
            Component owner = (source instanceof Component) ? (Component) source : null;
            int option = JOptionPane.showConfirmDialog(owner, "Really Exit?");
            LOGGER.debug("-- opted to quit? {}", option);
            return (option == JOptionPane.YES_OPTION);
        }

        @Override
        public void willExit(EventObject e) {
            // cleanup
            LOGGER.debug(">> willExit()");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Main method">
    /**
     * Main method launching the application.
     * @param args 
     */
    public static void main(String[] args) {
        LOGGER.debug(">> main()");
        LOGGER.debug("-- JAVA.HOME: {}", System.getProperty("java.home"));
        LOGGER.debug("-- Library path: {}", System.getProperty("java.library.path"));
        try {
            launch(Main.class, args);
        } catch (Exception e) {
            LOGGER.debug("-- Catching exception in main: {}", e);
        }
        LOGGER.debug("<< main()");
    }
    //</editor-fold>
}
