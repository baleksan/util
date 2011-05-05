package com.baleksan.util.collections;


import java.util.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface Tree<N extends Comparable> extends Comparable<Tree<N>> {
    void setRoot(N rootValue);

    /**
     * Returns the parent of just inserted node, or null if the node cannot be inserted in the tree.
     * If the newNode becomes root then we just return that node.
     *
     * @param newValue new value to be inserted
     * @return the parent of just inserted node, or null if the node cannot be inserted in the tree.
     */
    TreeNode<N> add(N newValue);

    boolean canAdd(N value);

    boolean containsValue(N value);

    void remove(N value);

    void removeSubtree(N value);

    List<N> dfs();

    int size();

    TreeNode<N> getRoot();

    boolean canMergeWith(Tree<N> otherTree);

    void mergeWith(Tree<N> otherTree);
}
