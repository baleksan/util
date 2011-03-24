package com.baleksan.util.system;

import java.io.IOException;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface ShutdownHooked {
    /**
     * Method to be invoked on system shutdown.
     * 
     * @throws IOException when problems in shutdown
     */
    void onShutdown() throws IOException;
}
