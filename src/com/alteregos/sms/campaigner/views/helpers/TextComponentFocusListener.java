package com.alteregos.sms.campaigner.views.helpers;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author John Emmanuel
 */
public class TextComponentFocusListener implements FocusListener {

    private JTextComponent component;

    public TextComponentFocusListener() {
    }

    public TextComponentFocusListener(JTextComponent component) {
        this.component = component;
    }

    @Override
    public void focusGained(FocusEvent e) {
        component.setBackground(new Color(255, 255, 255));
    }

    @Override
    public void focusLost(FocusEvent e) {
        //
    }
}
