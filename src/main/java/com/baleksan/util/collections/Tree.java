package com.baleksan.util.collections;


import java.util.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface Tree<N> {
    void setRoot(N rootValue);

    void add(N newValue);

    void remove(N value);

    void removeSubtree(N value);

    List<N> dfs();

    int size();
}
