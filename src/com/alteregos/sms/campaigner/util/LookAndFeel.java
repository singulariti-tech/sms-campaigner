/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.util;

/**
 *
 * @author John Emmanuel
 */
public enum LookAndFeel {

    BUSINESS_BLACK_STEEL("Black Steel Business", "org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel"),
    BUSINESS_BLUE_STEEL("Blue Steel Business", "org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel"),
    CREME("Creme", "org.jvnet.substance.skin.SubstanceCremeLookAndFeel"),
    FIELD_OF_WHEAT("Field of Wheat", "org.jvnet.substance.skin.SubstanceFieldOfWheatLookAndFeel"),
    MIST_AQUA("Mist Aqua", "org.jvnet.substance.skin.SubstanceMistAquaLookAndFeel"),
    NEBULA_BRICK_WALL("Nebula Brick Wall", "org.jvnet.substance.skin.SubstanceNebulaBrickWallLookAndFeel");
    private String label;
    private String className;

    LookAndFeel(String label, String className) {
        this.label = label;
        this.className = className;
    }

    public String getLabel() {
        return label;
    }

    public String getClassName() {
        return className;
    }

    public static LookAndFeel getLookAndFeelFromLabel(String label) {
        for (LookAndFeel lnf : values()) {
            if (lnf.getLabel().equals(label)) {
                return lnf;
            }
        }
        return null;
    }

    public static LookAndFeel getLookAndFeelFromClassName(String className) {
        for (LookAndFeel lnf : values()) {
            if (lnf.getClassName().equals(className)) {
                return lnf;
            }
        }
        return null;
    }
}
