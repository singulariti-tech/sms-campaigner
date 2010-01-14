package com.alteregos.sms.campaigner.engine.processors.helpers;

/**
 *
 * @author John Emmanuel
 */
public class RuleProcessResult {

    private String content = "";
    private boolean dndRule = false;
    private boolean rule = false;

    public RuleProcessResult() {
    }

    public RuleProcessResult(String content) {
        this.content = content;
    }

    public RuleProcessResult(boolean isDndRule, String content) {
        this.content = content;
        this.dndRule = isDndRule;
    }

    public String getContent() {
        return content;
    }

    public boolean isDndRule() {
        return dndRule;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDndRule(boolean dndRule) {
        this.dndRule = dndRule;
    }

    public boolean isRule() {
        return rule;
    }

    public void setRule(boolean rule) {
        this.rule = rule;
    }
}
