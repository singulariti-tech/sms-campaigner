package com.alteregos.sms.campaigner.rules;

/**
 * @author john.emmanuel
 */
public class DndRule implements IRule {

    private String primaryKeyword;
    private String secondaryKeyword;
    private boolean enabled;
    private String content;

    public DndRule () {
    }

    public void setPrimaryKeyword (String primaryKeyword) {
        this.primaryKeyword = primaryKeyword;
    }

    public void setSecondaryKeyword (String secondaryKeyword) {
        this.secondaryKeyword = secondaryKeyword;
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }

    public void setContent (String content) {
        this.content = content;
    }

    @Override
    public boolean isEnabled () {
        return enabled;
    }

    @Override
    public String getContent () {
        return content;
    }

    @Override
    public String getPrimaryKeyword () {
        return primaryKeyword;
    }

    @Override
    public String getSecondaryKeyword () {
        return secondaryKeyword;
    }
}
