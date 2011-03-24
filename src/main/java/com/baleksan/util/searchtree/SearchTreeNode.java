package com.baleksan.util.searchtree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SearchTreeNode<V> {
    private SearchTreeNode<V> parent;
    private Map<V, SearchTreeNode<V>> children;
    private V value;
    private boolean isTerminalNode;

    public SearchTreeNode() {
        children = new HashMap<V, SearchTreeNode<V>>();
    }

    public SearchTreeNode(V value) {
        this.value = value;
        children = new HashMap<V, SearchTreeNode<V>>();
    }

    public SearchTreeNode<V> getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void setParent(SearchTreeNode<V> parent) {
        this.parent = parent;
    }

    public void addChild(SearchTreeNode<V> child) {
        children.put(child.getValue(), child);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void addSequence(V... sequence) {
        V head = sequence[0];

        SearchTreeNode<V> child = children.containsKey(head) ? children.get(head) : new SearchTreeNode<V>(head);
        if (!children.containsKey(head)) {
            addChild(child);
            child.setParent(this);
        }

        if (sequence.length > 1) {
            V[] rest = (V[]) Array.newInstance(sequence.getClass().getComponentType(), sequence.length - 1);
            System.arraycopy(sequence, 1, rest, 0, sequence.length - 1);
            child.addSequence(rest);
        }

        if (sequence.length == 1) {
            child.setTerminalNode(true);
        }

    }

    public boolean isTerminalNode() {
        return isTerminalNode;
    }

    public void setTerminalNode(boolean terminalNode) {
        isTerminalNode = terminalNode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(value);
        builder.append("{");
        for (V childValue : children.keySet()) {
            builder.append(childValue);
            builder.append(",");
        }
        builder.append("}");


        return builder.toString();
    }

    public boolean accept(List<V> sequence) {
        if (sequence.size() == 0 && !isTerminalNode()) {
            return false;
        }
        if (sequence.size() == 0 && isTerminalNode()) {
            return true;
        }

        V head = sequence.get(0);
        if (!children.containsKey(head)) {
            return false;
        }

        List<V> tail = sequence.subList(1, sequence.size());
        SearchTreeNode<V> child = children.get(head);

        return child.accept(tail);
    }

    public List<V> acceptPrefix(List<V> sequence) {
        if(sequence.size() == 0 || isTerminalNode()) {
           return constructPath();
        }

        V head = sequence.get(0);
        if (!children.containsKey(head)) {
            return null;
        }

        List<V> tail = sequence.subList(1, sequence.size());

        SearchTreeNode<V> child = children.get(head);

        return child.acceptPrefix(tail);
    }

    private List<V> constructPath() {
        List<V> inverse = new ArrayList<V>();
        SearchTreeNode<V> me = this;
        while(true) {
            inverse.add(me.getValue());

            SearchTreeNode<V> parent = me.getParent();
            if(parent.isRoot()) {
                break;
            }

            me = parent;
        }

        Collections.reverse(inverse);

        return inverse;
    }
}
