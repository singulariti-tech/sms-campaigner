package com.alteregos.sms.campaigner.services.probe;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.conf.Configuration;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskListener;

/**
 *
 * @author John Emmanuel
 */
public class ProbeTask extends Task<Object, Void> {

    private ProbeTool probeTool;
    private Configuration configuration;

    public ProbeTask(Application app, Configuration configuration) {
        super(app);
        setUserCanCancel(false);
        this.configuration = configuration;
        this.probeTool = new ProbeTool(configuration);
    }

    @Override
    protected Object doInBackground() throws Exception {
        return this.probeTool.start();
    }

    @Override
    public void addTaskListener(TaskListener<Object, Void> taskListener) {
        super.addTaskListener(taskListener);
    }

    @Override
    protected void succeeded(Object object) {
        super.succeeded(object);
        ProbeResults results = (ProbeResults) object;
        Main.getApplication().setProbeResults(results);
    }
}
