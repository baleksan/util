package com.baleksan.util.collections;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class Tree<N> {
    private TreeNode<N> root;
    private Map<TreeNode<N>, TreeNode<N>> childParentMap;
    private TreeInserter<N> inserter;

    public Tree(TreeInserter<N> inserter) {
        this.inserter = inserter;
        childParentMap = new HashMap<TreeNode<N>, TreeNode<N>>();
    }

    public void setRoot(N rootValue) {
        setRoot(new TreeNode<N>(rootValue));
    }

    private void setRoot(TreeNode<N> root) {
        this.root = root;
    }

    public void add(N newValue) {
        addNode(new TreeNode<N>(newValue));
    }

    private void addNode(TreeNode<N> newNode) {
        if (root == null) {
            root = newNode;
            childParentMap.put(root, null);
            return;
        }

        TreeNode<N> parent = addNode(root, newNode);
        if (parent == null) {
            throw new IllegalArgumentException("Cannot insert a node " + newNode);
        }
        childParentMap.put(newNode, parent);
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

    public void removeNode(TreeNode<N> node) {
        removeNode(node, true);
    }

    public void removeSubtree(TreeNode<N> node) {
        removeNode(node, false);

    }

    private void removeNode(TreeNode<N> node, boolean promoteGrandchildren) {
        TreeNode<N> parent = childParentMap.get(node);
        if (parent == null) {
            throw new IllegalArgumentException("Cannot remove root");
        }

        parent.removeChild(node, promoteGrandchildren);
        childParentMap.remove(node);
    }

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
}
