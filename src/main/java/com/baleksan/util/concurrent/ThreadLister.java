package com.baleksan.util.concurrent;

/**
 * Utility to list running threads.
 */
public class ThreadLister {

    public static interface ThreadHandler {
        void thread(Thread t);
    }

    public static void listThreads(ThreadHandler h) {
        // Find the root thread group
        ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
        while (root.getParent() != null) {
            root = root.getParent();
        }

        // Visit each thread group
        visit(root, 0, h);
    }

    // This method recursively visits all thread groups under `group'.
    public static void visit(ThreadGroup group, int level, ThreadHandler h) {
        // Get threads in `group'
        int numThreads = group.activeCount();
        Thread[] threads = new Thread[numThreads * 2];
        numThreads = group.enumerate(threads, false);

        // Enumerate each thread in `group'
        for (int i = 0; i < numThreads; i++) {
            // Get thread
            Thread thread = threads[i];
            h.thread(thread);
        }

        // Get thread subgroups of `group'
        int numGroups = group.activeGroupCount();
        ThreadGroup[] groups = new ThreadGroup[numGroups * 2];
        numGroups = group.enumerate(groups, false);

        // Recursively visit each subgroup
        for (int i = 0; i < numGroups; i++) {
            visit(groups[i], level + 1, h);
        }
    }
}
