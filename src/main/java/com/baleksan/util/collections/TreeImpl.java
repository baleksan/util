package com.baleksan.util.collections;

import java.util.*;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TreeImpl<N> implements Tree<N> {
    private TreeNode<N> root;
    private Map<N, List<TreeNode<N>>> childParentMap;
    private TreeInserter<N> inserter;

    public TreeImpl(TreeInserter<N> inserter) {
        this.inserter = inserter;
        childParentMap = new HashMap<N, List<TreeNode<N>>>();
    }

    @Override
    public void setRoot(N rootValue) {
        setRoot(new TreeNode<N>(rootValue));
    }

    private void setRoot(TreeNode<N> root) {
        this.root = root;
        childParentMap.put(root.getValue(), new ArrayList<TreeNode<N>>());
    }

    @Override
    public void add(N newValue) {
        addNode(new TreeNode<N>(newValue));
    }

    private void addNode(TreeNode<N> newNode) {
        if (root == null) {
            root = newNode;
            childParentMap.put(root.getValue(), new ArrayList<TreeNode<N>>());
            return;
        }

        TreeNode<N> parent = addNode(root, newNode);
        if (parent == null) {
            throw new IllegalArgumentException("Cannot insert a node " + newNode);
        }

        List<TreeNode<N>> prevValue = childParentMap.containsKey(newNode.getValue()) ?
                childParentMap.get(newNode.getValue()) : new ArrayList<TreeNode<N>>();
        prevValue.add(parent);
        childParentMap.put(newNode.getValue(), prevValue);
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
    public void remove(N value) {
        removeNode(value, true);
    }

    @Override
    public void removeSubtree(N value) {
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
    public List<N> dfs() {
        List<N> result = new ArrayList<N>();
        result = dfs(result, root);

        return result;
    }

    private List<N> dfs(List<N> result, TreeNode<N> root) {
        result.add(root.getValue());
        if (root.hasChildren()) {
            for (TreeNode<N> child : root.getChildren()) {
                dfs(result, child);
            }
        }
        return result;
    }

    @Override
    public int size() {
        return childParentMap.size();
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
}

