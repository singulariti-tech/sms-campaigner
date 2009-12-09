package com.alteregos.sms.campaigner.util;

/**
 *
 * @author John Emmanuel
 */
public enum LookAndFeel {

    AUTUMN("Autumn", "org.pushingpixels.substance.api.skin.SubstanceAutumnLookAndFeel"),
    BUSINESS_BLACK_STEEL("Black Steel Business", "org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel"),
    BUSINESS_BLUE_STEEL("Blue Steel Business", "org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel"),
    BUSINESS("Business", "org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel"),
    CHALLENGER_DEEP("Challenger Deep", "org.pushingpixels.substance.api.skin.SubstanceChallengerDeepLookAndFeel"),
    CREME_COFFEE("Creme Coffee", "org.pushingpixels.substance.api.skin.SubstanceCremeCoffeeLookAndFeel"),
    CREME("Creme", "org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel"),
    DUST_COFFEE("Dust Coffee", "org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel"),
    DUST("Dust", "org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel"),
    EMERALD_DUSK("Emerald Dusk", "org.pushingpixels.substance.api.skin.SubstanceEmeraldDuskLookAndFeel"),
    GEMINI("Gemini", "org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel"),
    MEGALLAN("Megallan", "org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel"),
    MIST_AQUA("Mist Aqua", "org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel"),
    MIST_SILVER("Mist Silver", "org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel"),
    MODERATE("Moderate", "org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel"),
    NEBULA("Nebula", "org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel"),
    NEBULA_BRICK_WALL("Nebula Brick Wall", "org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel"),
    OFFICE_BLUE_2007("Office 2007 Blue", "org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel"),
    OFFICE_SILVER_2007("Office 2007 Silver", "org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel"),    
    RAVEN("Raven", "org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel"),    
    SAHARA("Sahara", "org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel"),
    TWILIGHT("Twilight", "org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel");
    //
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
