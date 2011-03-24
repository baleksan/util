package com.baleksan.util.system;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SystemManagerFactory {
    private static SystemManager systemManager;

    public static SystemManager getSystemManager() {
        synchronized (SystemManagerFactory.class) {
            if (systemManager == null) {
                systemManager = new SystemManager();
            }
        }

        return systemManager;
    }
}
