/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.helpers;

import com.alteregos.sms.campaigner.Main;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author John Emmanuel
 */
public class AbsoluteWindowListener extends WindowAdapter {

    public AbsoluteWindowListener() {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Main.getApplication().quit(null);
    }
}
