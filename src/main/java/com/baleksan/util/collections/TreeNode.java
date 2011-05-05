package com.baleksan.util.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TreeNode<N extends Comparable> implements Comparable<TreeNode<N>> {
    public N value;

    public TreeNode<N> parent;
    public List<TreeNode<N>> children;

    public TreeNode(N value) {
        this.value = value;
        children = new ArrayList<TreeNode<N>>();
    }

    public List<TreeNode<N>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public synchronized void addChild(TreeNode<N> child) {
        children.add(child);
    }

    public synchronized void addChildAt(TreeNode<N> child, int position) {
        children.set(position, child);
    }

    public N getValue() {
        return value;
    }

    public void setValue(N value) {
        this.value = value;
    }

    public TreeNode<N> getParent() {
        return parent;
    }

    public synchronized void removeChild(N value, boolean promoteGrandchildren) {
        TreeNode<N> nodeToRemove = null;
        List<TreeNode<N>> nodesToAdd = new ArrayList<TreeNode<N>>();
        for (TreeNode<N> child : children) {
            if (child.getValue().equals(value)) {
                if (promoteGrandchildren) {
                    //promote children of the removed node up
                    for (TreeNode<N> grandChild : child.getChildren()) {
                        nodesToAdd.add(grandChild);
                    }
                }

                nodeToRemove = child;
            }
        }

        if (nodeToRemove != null) {
            children.remove(nodeToRemove);
        }

        for (TreeNode<N> node : nodesToAdd) {
            children.add(node);
        }
    }

    public void setParent(TreeNode<N> parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(value.toString());
        builder.append("{");
        builder.append(parent == null ? "null" : parent.getValue().toString());
        builder.append("} - > [");

        synchronized (this) {
            for (TreeNode<N> child : children) {
                builder.append(child.toString());
                builder.append(", ");
            }
        }
        builder.append("]");

        return builder.toString();
    }

    public synchronized boolean hasChildren() {
        return !children.isEmpty();
    }

    public int getNumDescendants() {
        int numDescendants = 0;
        for (TreeNode<N> child : getChildren()) {
            numDescendants += child.getNumDescendants();
        }
        return numDescendants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode treeNode = (TreeNode) o;

        return !(value != null ? !value.equals(treeNode.value) : treeNode.value != null);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public int compareTo(TreeNode<N> o) {
        return value.compareTo(o.getValue());
    }
}
