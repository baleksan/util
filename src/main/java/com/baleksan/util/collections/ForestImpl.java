package com.baleksan.util.collections;

import java.util.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class ForestImpl<N extends Comparable> implements Forest<N> {
    private List<Tree<N>> trees;
    private TreeInserter<N> inserter;

    private final Object treesLock = new Object();

    public ForestImpl(TreeInserter<N> inserter) {
        this.inserter = inserter;
        trees = new ArrayList<Tree<N>>();
    }

    /**
     * All trees in the forest are checked for the new node to be eligible to be added to the tree. If none of the
     * trees can hold it, then the new sapling is added to the forest.
     *
     * @param newValue new value to be added.
     */
    @Override
    public void add(N newValue) {
        for (Tree<N> tree : trees) {
            if (tree.add(newValue) != null) {
                return;
            }
        }

        //could not add to any existing trees, create a new one
        Tree<N> newTree = new TreeImpl<N>(inserter);
        newTree.add(newValue);

        synchronized (treesLock) {
            trees.add(newTree);
            Collections.sort(trees);
        }
    }

    @Override
    public void remove(N value) {
        for (Tree<N> tree : trees) {
            if (tree.containsValue(value)) {
                tree.remove(value);
                return;
            }
        }
    }

    @Override
    public List<N> dfs() {
        List<N> result = new ArrayList<N>();

        synchronized (treesLock) {
            for (Tree<N> tree : trees) {
                result.addAll(tree.dfs());
            }
        }

        return result;
    }

    @Override
    public List<Tree<N>> getTrees() {
        return Collections.unmodifiableList(trees);
    }

    /**
     * This algorithm is quadratic, so use with caution, especially with large forests.
     */
    @Override
    public void mergeTrees() {
        Set<TreesToMerge> mergeCandidates = new HashSet<TreesToMerge>();

        synchronized (treesLock) {
            for (Tree<N> tree1 : trees) {
                for (Tree<N> tree2 : trees) {
                    if (!tree1.equals(tree2)) {
                        if (tree1.canMergeWith(tree2)) {
                            mergeCandidates.add(new TreesToMerge(tree1, tree2));
                        }
                        if (tree2.canMergeWith(tree1)) {
                            mergeCandidates.add(new TreesToMerge(tree2, tree1));
                        }
                    }
                }
            }

            for (TreesToMerge mergeDuple : mergeCandidates) {
                mergeDuple.firstTree.mergeWith(mergeDuple.secondTree);
                trees.remove(mergeDuple.secondTree);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForestImpl forest = (ForestImpl) o;

        if (trees != null ? !trees.equals(forest.trees) : forest.trees != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return trees != null ? trees.hashCode() : 0;
    }

    class TreesToMerge {
        Tree<N> firstTree;
        Tree<N> secondTree;

        TreesToMerge(Tree<N> firstTree, Tree<N> secondTree) {
            this.firstTree = firstTree;
            this.secondTree = secondTree;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TreesToMerge that = (TreesToMerge) o;

            if (firstTree != null ? !firstTree.equals(that.firstTree) : that.firstTree != null) return false;
            if (secondTree != null ? !secondTree.equals(that.secondTree) : that.secondTree != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = firstTree != null ? firstTree.hashCode() : 0;
            result = 31 * result + (secondTree != null ? secondTree.hashCode() : 0);
            return result;
        }
    }
}
