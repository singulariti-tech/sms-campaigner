/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.data.beans.DndRule;
import com.alteregos.sms.campaigner.data.beans.IRule;
import com.alteregos.sms.campaigner.data.beans.Rule;
import com.alteregos.sms.campaigner.engine.processors.helpers.RuleProcessResult;
import com.alteregos.sms.campaigner.util.LoggerHelper;
import com.alteregos.sms.campaigner.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author John Emmanuel
 */
public class RuleProcessor {

    private static Logger log = LoggerHelper.getLogger();
    private IRule dndRule;
    private EntityManager entityManager;
    private Query ruleQuery;

    public RuleProcessor() {
        dndRule = new DndRule();
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("absolute-smsPU").createEntityManager();
        ruleQuery = entityManager.createQuery("SELECT r FROM Rule r");
    }

    public RuleProcessResult process(String content) {
        log.debug("Processing rule content");
        RuleParseResult parseResult = parse(content);
        MatchedRule matchedRule = new MatchedRule();
        boolean isDnd = false;
        String ruleContent = "";
        if (parseResult.isRule()) {
            matchedRule = matchRule(parseResult);
            if (matchedRule.getRule() != null) {
                ruleContent = matchedRule.getRule().getContent();
            } else {
                log.debug("Could not match any rule");
            }
            isDnd = matchedRule.isDnd();
        }
        RuleProcessResult result = new RuleProcessResult(ruleContent);
        result.setRule(parseResult.isRule());
        if (isDnd) {
            result.setDndRule(isDnd);
        }
        return result;
    }

    //TODO Verify if ruleSecondary is null anytime
    private MatchedRule matchRule(RuleParseResult parseResult) {
        for (Rule rule : getRules()) {
            String rulePrimary = rule.getPrimaryKeyword().toLowerCase();
            String ruleSecondary = rule.getSecondaryKeyword().toLowerCase();
            String resultPrimary = parseResult.getPrimary().toLowerCase();
            String resultSecondary = parseResult.getSecondary().toLowerCase();

            boolean isDnd = matchesDnd(parseResult);
            if (rulePrimary.equals(resultPrimary) && ruleSecondary.equals(resultSecondary)) {
                log.debug("Rule matched - primary: " + rulePrimary + " secondary: " + ruleSecondary);
                MatchedRule matchedRule = new MatchedRule(rule);
                if (isDnd) {
                    matchedRule.setDnd(isDnd);
                    matchedRule.setRule(dndRule);
                    log.debug("Matched DND rule");
                }
                return matchedRule;
            }
        }
        return new MatchedRule();
    }

    private boolean matchesDnd(RuleParseResult parseResult) {
        boolean matches = false;
        String resultPrimary = parseResult.getPrimary().toLowerCase();
        String resultSecondary = parseResult.getSecondary().toLowerCase();
        if (resultPrimary.equals(dndRule.getPrimaryKeyword().toLowerCase()) &&
                resultSecondary.equals(dndRule.getSecondaryKeyword().toLowerCase())) {
            return true;
        }
        return matches;
    }

    private RuleParseResult parse(String content) {
        RuleParseResult result = new RuleParseResult();
        String delimiter = "~:~";
        String delimitedString = StringUtils.stripSpaces(content, delimiter);
        String[] parsedContent = delimitedString.split(delimiter);
        if (parsedContent.length == 1) {
            log.debug("Matched ONE word content");
            result.setPrimary(parsedContent[0]);
            result.setRule(true);
        } else if (parsedContent.length == 2) {
            log.debug("Matched TWO word content");
            result.setPrimary(parsedContent[0]);
            result.setSecondary(parsedContent[1]);
            result.setRule(true);
        } else {
            log.debug("More than 2 words in content. May not be rule based SMS");
            if (parsedContent.length > 5) {
                result.setRule(false);
            } else {
                result.setRule(true);
            }
        }
        return result;
    }

    private List<Rule> getRules() {
        return filter((List<Rule>) ruleQuery.getResultList());
    }

    private List<Rule> filter(List<Rule> inputList) {
        List<Rule> outputList = new ArrayList<Rule>();
        for (Rule rule : inputList) {
            if (rule.getEnabled()) {
                outputList.add(rule);
            }
        }
        return outputList;
    }

    private class RuleParseResult {

        private String primary = "";
        private String secondary = "";
        private boolean rule = false;

        public RuleParseResult() {
        }

        public RuleParseResult(String primary, String secondary) {
            this.primary = primary;
            this.secondary = secondary;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getSecondary() {
            return secondary;
        }

        public void setSecondary(String secondary) {
            this.secondary = secondary;
        }

        public boolean isRule() {
            return rule;
        }

        public void setRule(boolean rule) {
            this.rule = rule;
        }
    }

    private class MatchedRule {

        private IRule rule = null;
        private boolean dnd = false;

        public MatchedRule() {
        }

        public MatchedRule(IRule rule) {
            this.rule = rule;
        }

        public MatchedRule(IRule rule, boolean isDnd) {
            this.rule = rule;
            this.dnd = isDnd;
        }

        public boolean isDnd() {
            return dnd;
        }

        public IRule getRule() {
            return rule;
        }

        public void setDnd(boolean dnd) {
            this.dnd = dnd;
        }

        public void setRule(IRule rule) {
            this.rule = rule;
        }
    }
}
