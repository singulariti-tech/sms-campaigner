/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.helpers;

import com.alteregos.sms.campaigner.views.BlockerDialog;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.jdesktop.application.Task.InputBlocker;

/**
 *
 * @author John Emmanuel
 */
public class ProbeInputBlocker extends InputBlocker {

    private BlockerDialog dialog;

    public ProbeInputBlocker(Task task, BlockerDialog dialog) {
        super(task, BlockingScope.APPLICATION, dialog);
        this.dialog = dialog;
        dialog.setVisible(true);
    }

    @Override
    protected void block() {
        dialog.setMessage("Checking...");
    }

    @Override
    protected void unblock() {
        dialog.dispose();
    }
}
