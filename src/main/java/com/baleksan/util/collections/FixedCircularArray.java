package com.baleksan.util.collections;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class FixedCircularArray<T> {
    private T[] array;
    private AtomicInteger index;

    public FixedCircularArray(List<T> list) {
        array = (T[]) list.toArray();
        index = new AtomicInteger(0);
    }

    public T pickNext() {
        if (index.get() == array.length) {
            for (; ;) {
                int current = index.get();
                if (index.compareAndSet(current, 0)) {
                    break;
                }
            }
        }

        T value = array[index.get()];
        index.incrementAndGet();

        return value;
    }
}
