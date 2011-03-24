package com.baleksan.util.filter;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface Filter<K> {
    boolean accept(K key);
}
