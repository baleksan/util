package com.baleksan.util.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TreeNode<N> {
    public N value;

    public TreeNode<N> parent;
    public List<TreeNode<N>> children;

    public TreeNode(N value) {
        this.value = value;
        children = new ArrayList<TreeNode<N>>();
    }

    public List<TreeNode<N>> getChildren() {
        return children;
    }

    public void addChild(TreeNode<N> child) {
        children.add(child);
    }

    public void addChildAt(TreeNode<N> child, int position) {
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

    public void removeChild(TreeNode<N> node, boolean promoteGrandchildren) {
        if (!children.contains(node)) {
            return;
        }

        if (promoteGrandchildren) {
            //promote children of the removed node up
            for (TreeNode<N> child : node.getChildren()) {
                children.add(child);
            }
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
        for (TreeNode<N> child : children) {
            builder.append(child.toString());
            builder.append(", ");
        }
        builder.append("]");

        return builder.toString();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
