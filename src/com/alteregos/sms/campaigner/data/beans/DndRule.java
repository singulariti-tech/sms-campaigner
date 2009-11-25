/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.data.beans;

/**
 *
 * @author John Emmanuel
 */
public class DndRule implements IRule {

    private final String PRIMARY = "start";
    private final String SECONDARY = "DND";
    private String content = "You have chosen not to receive messages from us. " +
            "Effective now you will not receive any messages. Your reference no. is ";

    public DndRule() {
    }

    public DndRule(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public boolean getEnabled() {
        return true;
    }

    public String getPrimaryKeyword() {
        return PRIMARY;
    }

    public String getSecondaryKeyword() {
        return SECONDARY;
    }
}
