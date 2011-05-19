package com.baleksan.util.concurrent;

import org.apache.lucene.util.ThreadInterruptedException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

/**
 * A helper class that wraps a {@link java.util.concurrent.CompletionService} and provides an
 * iterable interface to the completed {@link Callable} instances.
 * Lifted from ParallelMultiSearcher
 *
 * @param <T> the type of the {@link Callable} return value
 */
public final class ExecutionHelper<T> implements Iterator<T>, Iterable<T> {
    private final CompletionService<T> service;
    private int numTasks;
    private long timeout;
    private TimeUnit unit;

    public ExecutionHelper(final Executor executor, long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
        this.service = new ExecutorCompletionService<T>(executor);
    }

    public ExecutionHelper(final Executor executor) {
        this.timeout = -1;
        this.unit = TimeUnit.MILLISECONDS;
        this.service = new ExecutorCompletionService<T>(executor);
    }

    public boolean hasNext() {
        return numTasks > 0;
    }

    public void submit(Callable<T> task) {
        this.service.submit(task);
        ++numTasks;
    }

    public T next() {
        if (!this.hasNext())
            throw new NoSuchElementException();
        try {
            Future<T> future = timeout > 0 ? service.poll(timeout, unit) : service.take();
            return future == null ? null : future.get();
        } catch (InterruptedException e) {
            throw new ThreadInterruptedException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            --numTasks;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator() {
        // use the shortcut here - this is only used in a private context
        return this;
    }

    public void clearTasks() {
        boolean interrupted = false;
        InterruptedException lastException = null;
        try {
            while (true) {
                Future<T> future = timeout > 0 ? service.poll(timeout, unit) : service.poll();
                if (future == null) {
                    break;
                }

                if (!future.isCancelled()) {
                    future.cancel(true);
                }
                numTasks--;
            }
        } catch (InterruptedException ignored) {
            interrupted = true;
            lastException = ignored;
        }

        if (interrupted) {
            throw new ThreadInterruptedException(lastException);
        }
    }
}
