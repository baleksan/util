package com.baleksan.util.collections;

import java.util.List;

/**
 * Forest is a collection of trees with the uniform interface to add a node to a forest. When a node
 * is added to a forest it is added to the first available tree. Periodically one can run a merge function
 * which will try to merge all eligiable trees.
 *
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface Forest<N extends Comparable> {
    void add(N newValue);

    void remove(N value);

    List<N> dfs();

    List<Tree<N>> getTrees();

    /**
     * Looks for possible merges inside the forest and modifies forest in-place. This algorithm is quadratic,
     * so use with caution, especially with large forests.
     */
    void mergeTrees();
}
