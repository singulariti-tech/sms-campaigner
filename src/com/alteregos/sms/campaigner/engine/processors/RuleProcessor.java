package com.alteregos.sms.campaigner.engine.processors;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.AutoReplyRule;
import com.alteregos.sms.campaigner.engine.processors.helpers.RuleProcessResult;
import com.alteregos.sms.campaigner.rules.DndRule;
import com.alteregos.sms.campaigner.rules.IRule;
import com.alteregos.sms.campaigner.services.RuleService;
import com.alteregos.sms.campaigner.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class RuleProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleProcessor.class);
    private IRule dndRule;
    private RuleService ruleService;

    public RuleProcessor() {
        LOGGER.debug("** RuleProcessor()");
        dndRule = new DndRule();
        ruleService = Main.getApplication().getBean("ruleService");
    }

    public RuleProcessResult process(String content) {
        LOGGER.debug(">> process()");
        RuleParseResult parseResult = parse(content);
        MatchedRule matchedRule = new MatchedRule();
        boolean isDnd = false;
        String ruleContent = "";
        if (parseResult.isRule()) {
            LOGGER.debug("-- parsed result is rule");
            matchedRule = matchRule(parseResult);
            if (matchedRule.getRule() != null) {
                ruleContent = matchedRule.getRule().getContent();
                LOGGER.debug("-- matched rule content: {}", ruleContent);
            } else {
                LOGGER.debug("-- could not match any rule");
            }
            isDnd = matchedRule.isDnd();
        }
        RuleProcessResult result = new RuleProcessResult(ruleContent);
        result.setRule(parseResult.isRule());
        if (isDnd) {
            result.setDndRule(isDnd);
        }
        LOGGER.debug("<< process()");
        return result;
    }

    //TODO Verify if ruleSecondary is null anytime
    private MatchedRule matchRule(RuleParseResult parseResult) {
        LOGGER.debug(">> matchRule()");
        String resultPrimary = parseResult.getPrimary().toLowerCase();
        String resultSecondary = parseResult.getSecondary().toLowerCase();
        for (IRule rule : getRules()) {
            String rulePrimary = rule.getPrimaryKeyword().toLowerCase();
            String ruleSecondary = rule.getSecondaryKeyword().toLowerCase();            

            boolean isDnd = matchesDnd(parseResult);
            if (rulePrimary.equals(resultPrimary) && ruleSecondary.equals(resultSecondary)) {
                LOGGER.debug("-- rule matched - primary: {} secondary: {}", rulePrimary, ruleSecondary);
                MatchedRule matchedRule = new MatchedRule(rule);
                if (isDnd) {
                    matchedRule.setDnd(isDnd);
                    matchedRule.setRule(dndRule);
                    LOGGER.debug("-- matched DND rule");
                }
                LOGGER.debug("<< matchRule()");
                return matchedRule;
            }
        }
        LOGGER.debug("-- no matching rule found");
        LOGGER.debug("<< matchRule()");
        return new MatchedRule();
    }

    private boolean matchesDnd(RuleParseResult parseResult) {
        LOGGER.debug(">> matchesDnd()");
        boolean matches = false;
        String resultPrimary = parseResult.getPrimary().toLowerCase();
        String resultSecondary = parseResult.getSecondary().toLowerCase();
        if (resultPrimary.equals(dndRule.getPrimaryKeyword().toLowerCase())
                && resultSecondary.equals(dndRule.getSecondaryKeyword().toLowerCase())) {
            return true;
        }
        LOGGER.debug("<< matchesDnd()");
        return matches;
    }

    private RuleParseResult parse(String content) {
        LOGGER.debug(">> parse()");
        RuleParseResult result = new RuleParseResult();
        String delimiter = "~:~";
        String delimitedString = StringUtils.stripSpaces(content, delimiter);
        String[] parsedContent = delimitedString.split(delimiter);
        if (parsedContent.length == 1) {
            LOGGER.debug("-- matched ONE word content");
            result.setPrimary(parsedContent[0]);
            result.setRule(true);
        } else if (parsedContent.length == 2) {
            LOGGER.debug("-- matched TWO word content");
            result.setPrimary(parsedContent[0]);
            result.setSecondary(parsedContent[1]);
            result.setRule(true);
        } else {
            LOGGER.debug("-- more than 2 words in content. may not be rule based SMS");
            if (parsedContent.length > 5) {
                result.setRule(false);
            } else {
                result.setRule(true);
            }
        }
        LOGGER.debug("<< parse()");
        return result;
    }

    private List<AutoReplyRule> getRules() {
        return filter(ruleService.getRules());
    }

    private List<AutoReplyRule> filter(List<AutoReplyRule> inputList) {
        List<AutoReplyRule> outputList = new ArrayList<AutoReplyRule>();
        for (AutoReplyRule rule : inputList) {
            if (rule.isEnabled()) {
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

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("RuleParseResult [");
            builder.append("primary=");
            builder.append(primary);
            builder.append(", rule=");
            builder.append(rule);
            builder.append(", secondary=");
            builder.append(secondary);
            builder.append("]");
            return builder.toString();
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

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("MatchedRule [");
            builder.append("dnd=");
            builder.append(dnd);
            builder.append(", rule=");
            builder.append(rule);
            builder.append("]");
            return builder.toString();
        }
    }
}
