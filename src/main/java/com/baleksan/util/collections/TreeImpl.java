package com.baleksan.util.collections;

import java.util.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TreeImpl<N extends Comparable> implements Tree<N> {
    private TreeNode<N> root;
    private Map<N, List<TreeNode<N>>> childParentMap;
    private TreeInserter<N> inserter;

    public TreeImpl(TreeInserter<N> inserter) {
        this.inserter = inserter;
        childParentMap = new HashMap<N, List<TreeNode<N>>>();
    }

    @Override
    public synchronized void setRoot(N rootValue) {
        setRoot(new TreeNode<N>(rootValue));
    }

    private void setRoot(TreeNode<N> root) {
        this.root = root;
        childParentMap.put(root.getValue(), new ArrayList<TreeNode<N>>());
    }

    @Override
    public synchronized boolean canAdd(N value) {
        return root == null || canAdd(root, value);
    }

    private boolean canAdd(TreeNode<N> nodeToInsertTo, N value) {
        if (inserter.insertHere(nodeToInsertTo.getValue(), value)) {
            return true;
        } else {
            for (TreeNode<N> child : nodeToInsertTo.getChildren()) {
                boolean sucess = canAdd(child, value);
                if (sucess) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public synchronized TreeNode<N> add(N newValue) {
        return addNode(new TreeNode<N>(newValue));
    }

    @Override
    public boolean containsValue(N value) {
        return childParentMap.containsKey(value);
    }

    private TreeNode<N> addNode(TreeNode<N> newNode) {
        if (root == null) {
            root = newNode;
            childParentMap.put(root.getValue(), new ArrayList<TreeNode<N>>());
            return root;
        }

        TreeNode<N> parent = addNode(root, newNode);
        if (parent == null) {
            return null;
        }

        List<TreeNode<N>> prevValue = childParentMap.containsKey(newNode.getValue()) ?
                childParentMap.get(newNode.getValue()) : new ArrayList<TreeNode<N>>();
        prevValue.add(parent);
        childParentMap.put(newNode.getValue(), prevValue);

        return parent;
    }

    private TreeNode<N> addNode(TreeNode<N> nodeToInsertTo, TreeNode<N> newNode) {
        if (inserter.insertHere(nodeToInsertTo.getValue(), newNode.getValue())) {
            nodeToInsertTo.addChild(newNode);
            newNode.setParent(nodeToInsertTo);
            return nodeToInsertTo;
        } else {
            for (TreeNode<N> child : nodeToInsertTo.getChildren()) {
                TreeNode<N> insertedNode = addNode(child, newNode);
                if (insertedNode != null) {
                    return insertedNode;
                }
            }
        }

        return null;
    }

    @Override
    public synchronized void remove(N value) {
        removeNode(value, true);
    }

    @Override
    public synchronized void removeSubtree(N value) {
        removeNode(value, false);
    }

    private void removeNode(N value, boolean promoteGrandchildren) {
        for (TreeNode<N> parent : childParentMap.get(value)) {
            if (parent == null) {
                throw new IllegalArgumentException("Cannot remove root");
            }

            parent.removeChild(value, promoteGrandchildren);
        }

        childParentMap.remove(value);
    }

    @Override
    public synchronized List<N> dfs() {
        List<N> result = new ArrayList<N>();
        result = dfs(result, root);

        return result;
    }

    private List<N> dfs(List<N> result, TreeNode<N> root) {
        if (root == null) {
            return result;
        }

        result.add(root.getValue());
        if (root.hasChildren()) {
            for (TreeNode<N> child : root.getChildren()) {
                dfs(result, child);
            }
        }
        return result;
    }

    @Override
    public synchronized int size() {
        return childParentMap.size();
    }

    @Override
    public TreeNode<N> getRoot() {
        return root;
    }

    /**
     * One can merge trees if one can insert a root node of the second tree into the first tree.
     *
     * @param otherTree other tree to test
     * @return true if the merge can succeed
     */
    @Override
    public synchronized boolean canMergeWith(Tree<N> otherTree) {
        TreeNode<N> otherRoot = otherTree.getRoot();
        return otherRoot != null && canAdd(otherRoot.getValue());
    }


    @Override
    public synchronized void mergeWith(Tree<N> otherTree) {
        for (N otherTreeNodeValue : otherTree.dfs()) {
            add(otherTreeNodeValue);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeImpl tree = (TreeImpl) o;

        if (root != null ? !root.equals(tree.root) : tree.root != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return root != null ? root.hashCode() : 0;
    }

    @Override
    public int compareTo(Tree<N> o) {
        if (root == null || o.getRoot() == null) {
            return 0;
        }
        return root.compareTo(o.getRoot());
    }
}

