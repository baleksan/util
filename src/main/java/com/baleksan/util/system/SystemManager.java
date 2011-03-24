package com.baleksan.util.system;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SystemManager {
    private static final Logger LOG = Logger.getLogger(SystemManager.class.getName());

    private Queue<ShutdownHooked> shutdownHookedList;

    public SystemManager() {
        shutdownHookedList = new ConcurrentLinkedQueue<ShutdownHooked>();
        ShutdownHookedThread hook = new ShutdownHookedThread();
        hook.setName("Shutdown hook");

        Runtime.getRuntime().addShutdownHook(hook);
    }

    public void registerShutdownHooked(ShutdownHooked shutdownProcessor) {
        shutdownHookedList.add(shutdownProcessor);
    }

    public void shutdown() {
        for (ShutdownHooked shutdown : shutdownHookedList) {
            try {
                shutdown.onShutdown();
            } catch (IOException e) {
                LOG.error("Cannot shutdown " + shutdown);
            }
        }

        LOG.info("Self-invoked shutdown complete!");
        System.exit(-1);
    }

    class ShutdownHookedThread extends Thread {
        @Override
        public void run() {
            for (ShutdownHooked hooked : shutdownHookedList) {
                try {
                    hooked.onShutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
