package com.baleksan.util.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */

public class StateInspector {
    private Set<StateAware> stateAwareSet;
    private AtomicBoolean allServicesIn = new AtomicBoolean(false);

    public StateInspector() {
        stateAwareSet = new HashSet<StateAware>();
    }

    public void addService(StateAware service) {
        stateAwareSet.add(service);
    }

    public void addServices(StateInspector otherInspector) {
        stateAwareSet.addAll(otherInspector.stateAwareSet);
    }

    public void allServicesIn() {
        allServicesIn.set(true);
    }

    public boolean areAllRunning() {
        while (true) {
            if (allServicesIn.get()) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        for (StateAware service : stateAwareSet) {
            if (!service.getState().equals(State.RUNNING)) {
                return false;
            }
        }

        return true;
    }
}
