package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.engine.processors.CallProcessor;
import com.alteregos.sms.campaigner.engine.processors.SmsProcessor;
import com.alteregos.sms.campaigner.engine.senders.SmsSender;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.smslib.Service;

/**
 *
 * @author John Emmanuel
 */
public class EngineServicesTask extends Task<Object, Void> {

    private static Logger log = LoggerHelper.getLogger();
    private CallProcessor callProcessor;
    private SmsProcessor smsProcessor;
    private SmsSender smsSender;
    private boolean callNotificationEnabled = false;

    public EngineServicesTask(Application application, Service service) {
        super(application);
        log.debug("Initializing engine services");
        setUserCanCancel(true);
        callProcessor = new CallProcessor();
        smsProcessor = new SmsProcessor();
        smsSender = new SmsSender(service);
        Configuration configuration = Main.getApplication().getConfiguration();
        if (configuration.isCallNotificationEnabled()) {
            callNotificationEnabled = true;
            String callNotificationMessage = configuration.getCallNotification();
            if (configuration.isMessageFooterEnabledForCallNotification()) {
                log.debug("Call notification is enabled with message footer");
                callNotificationMessage = callNotificationMessage + configuration.getMessageFooter();
            } else {
                log.debug("Call notification is enabled without message footer");
            }
            callProcessor.setNotificationMessage(callNotificationMessage);
        }
        if (configuration.isDefaultMessageEnabled()) {
            String defaultMessage = configuration.getDefaultMessage();
            if (configuration.isMessageFooterEnabledForDefaultMessage()) {
                log.debug("Default message is enabled with message footer");
                defaultMessage = defaultMessage + configuration.getMessageFooter();
            } else {
                log.debug("Default message is enabled without message footer");
            }
            smsProcessor.setDefaultMessage(defaultMessage);
        }
    }

    @Override
    protected Object doInBackground() throws Exception {
        log.debug("Starting engine tasks");
        while (!isCancelled()) {
            if (callNotificationEnabled) {
                callProcessor.process();
            }
            smsProcessor.process();
            smsSender.send();
            Thread.sleep(500);
        }
        return null;
    }

    @Override
    protected void failed(Throwable arg0) {
        super.failed(arg0);
        log.error("SMS engine task failed");
    }

    @Override
    protected void finished() {
        super.finished();
        log.info("SMS engine task completed");
    }

    @Override
    protected void succeeded(Object arg0) {
        super.succeeded(arg0);
        log.info("SMS engine task succeded");
    }
}
