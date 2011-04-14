package com.baleksan.util.collections;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface TreeInserter<N> {

    /**
     * Return true if the given value should be inserted as a child of the value in the argument
     *
     * @param potentialParent value to check for potential insertion
     * @param newValue potential value to insert
     * @return true if the given value should be inserted as a child of the value in the argument
     */
    boolean insertHere(N potentialParent, N newValue);
}
