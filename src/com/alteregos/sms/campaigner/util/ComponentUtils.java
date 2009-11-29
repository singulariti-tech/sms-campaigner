package com.alteregos.sms.campaigner.util;

import java.awt.Insets;
import javax.swing.JTextField;

/**
 *
 * @author John Emmanuel
 */
public class ComponentUtils {

    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setMargin(new Insets(5, 5, 5, 5));
        return tf;
    }
}
