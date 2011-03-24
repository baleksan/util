package com.baleksan.util.searchtree;

import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SearchTree<V> {
    private int size;
    private SearchTreeNode<V> root;

    public SearchTreeNode<V> getRoot() {
        return root;
    }

    public void setRoot(SearchTreeNode<V> root) {
        this.root = root;
    }

    public void addSequence(V ... sequence) {
        root.addSequence(sequence);
        size++;
    }

    public boolean accept(List<V> sequence) {
        return root.accept(sequence);
    }

    public List<V> acceptPrefix(List<V> sequence) {
        return root.acceptPrefix(sequence);
    }

    public int size() {
        return size;
    }
}
