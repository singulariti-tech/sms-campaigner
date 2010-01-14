package com.alteregos.sms.campaigner.business;

import org.smslib.FailureCauses;

/**
 *
 * @author John Emmanuel
 */
public enum FailureCause {

    BAD_FORMAT("Bad message format", FailureCauses.BAD_FORMAT),
    BAD_NUMBER("Number either incorrect or not reachable", FailureCauses.BAD_NUMBER),
    GATEWAY_AUTH("", FailureCauses.GATEWAY_AUTH),
    GATEWAY_FAILURE("Gateway failure", FailureCauses.GATEWAY_FAILURE),
    NO_CREDIT("SIM card does not have credit", FailureCauses.NO_CREDIT),
    NO_ROUTE("Route to number unavailable", FailureCauses.NO_ROUTE),
    UNKNOWN("Unknown", FailureCauses.UNKNOWN);
    /**
     * 
     */
    private String label;
    private FailureCauses cause;

    FailureCause(String label, FailureCauses cause) {
        this.label = label;
        this.cause = cause;
    }

    public String getLabel() {
        return this.label;
    }

    public FailureCauses getCause() {
        return this.cause;
    }

    public static FailureCause getFailureCause(FailureCauses cause) {
        for (FailureCause failureCause : values()) {
            if (failureCause.getCause().equals(cause)) {
                return failureCause;
            }
        }
        return null;
    }
}
