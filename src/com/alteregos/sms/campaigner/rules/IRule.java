package com.alteregos.sms.campaigner.rules;

/**
 * @author john.emmanuel
 */
public interface IRule {
    boolean isEnabled();
    String getContent();
    String getPrimaryKeyword();
    String getSecondaryKeyword();
}
