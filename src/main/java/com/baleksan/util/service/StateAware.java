package com.baleksan.util.service;

/**
 * Defines a state machine for a particular service. Service implementing this interface report it state (if possible) to
 * the client.
 *
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface StateAware {
    State getState();
}
