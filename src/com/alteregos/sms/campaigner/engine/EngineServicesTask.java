package com.alteregos.sms.campaigner.engine;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import com.alteregos.sms.campaigner.engine.processors.CallProcessor;
import com.alteregos.sms.campaigner.engine.processors.SmsProcessor;
import com.alteregos.sms.campaigner.engine.senders.SmsSender;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.Service;

/**
 *
 * @author John Emmanuel
 */
public class EngineServicesTask extends Task<Object, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EngineServicesTask.class);
    private CallProcessor callProcessor;
    private SmsProcessor smsProcessor;
    private SmsSender smsSender;
    private boolean callNotificationEnabled = false;

    public EngineServicesTask(Application application, Service service) {
        super(application);
        LOGGER.debug("** EngineServicesTask()");
        setUserCanCancel(true);
        callProcessor = new CallProcessor();
        smsProcessor = new SmsProcessor();
        smsSender = new SmsSender(service);
        Configuration configuration = Main.getApplication().getConfiguration();
        if (configuration.isCallNotificationEnabled()) {
            callNotificationEnabled = true;
            String callNotificationMessage = configuration.getCallNotification();
            boolean messageFooterEnabledForCallNotification = configuration.isMessageFooterEnabledForCallNotification();
            LOGGER.info("-- call notifications enabled with message footer: {}", messageFooterEnabledForCallNotification);
            if (messageFooterEnabledForCallNotification) {
                callNotificationMessage = callNotificationMessage + configuration.getMessageFooter();
            }
            callProcessor.setNotificationMessage(callNotificationMessage);
        }
        if (configuration.isDefaultMessageEnabled()) {
            String defaultMessage = configuration.getDefaultMessage();
            boolean messageFooterEnabledForDefaultMessage = configuration.isMessageFooterEnabledForDefaultMessage();
            LOGGER.info("-- default message enabled with message footer? {}", messageFooterEnabledForDefaultMessage);
            if (messageFooterEnabledForDefaultMessage) {
                defaultMessage = defaultMessage + configuration.getMessageFooter();
            }
            smsProcessor.setDefaultMessage(defaultMessage);
        }
    }

    @Override
    protected Object doInBackground() throws Exception {
        LOGGER.debug(">> doInBackground()");
        while (!isCancelled()) {
            if (callNotificationEnabled) {
                callProcessor.process();
            }
            smsProcessor.process();
            smsSender.send();
            Thread.sleep(500);
        }
        LOGGER.debug("<< doInBackground()");
        return null;
    }

    @Override
    protected void failed(Throwable arg0) {
        super.failed(arg0);
        LOGGER.error(">> failed()");
    }

    @Override
    protected void finished() {
        super.finished();
        LOGGER.info(">> finished()");
    }

    @Override
    protected void succeeded(Object arg0) {
        super.succeeded(arg0);
        LOGGER.info(">> succeeded()");
    }
}
