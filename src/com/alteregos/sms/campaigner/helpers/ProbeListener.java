package com.alteregos.sms.campaigner.helpers;

import com.alteregos.sms.campaigner.services.probe.ProbeResults;

/**
 *
 * @author John Emmanuel
 */
public interface ProbeListener {
    void probeStarted();
    void probeEnded(ProbeResults results);
}
