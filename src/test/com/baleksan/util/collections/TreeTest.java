package com.baleksan.util.collections;

import com.baleksan.util.Util;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TreeTest {

    @Test
    public void testDFS1() {
        Tree<TestMessage> tree = new TreeImpl<TestMessage>(new TestInserter());
        tree.setRoot(new TestMessage(0, -1));
        tree.add(new TestMessage(1, 0));
        tree.add(new TestMessage(2, 0));
        tree.add(new TestMessage(3, 0));
        tree.add(new TestMessage(4, 1));
        tree.add(new TestMessage(5, 1));
        tree.add(new TestMessage(6, 2));
        tree.add(new TestMessage(7, 2));
        tree.add(new TestMessage(8, 4));
        tree.add(new TestMessage(9, 8));
        tree.add(new TestMessage(10, 9));

        List<TestMessage> dfsOrder = tree.dfs();
        Assert.assertEquals("0:-1 1:0 4:1 8:4 9:8 10:9 5:1 2:0 6:2 7:2 3:0", Util.list2Str(dfsOrder));
    }

    @Test
    public void testDFS2() {
        Tree<TestMessage> tree = new TreeImpl<TestMessage>(new TestInserter());
        tree.setRoot(new TestMessage(0, -1));
        tree.add(new TestMessage(1, 0));
        tree.add(new TestMessage(2, 0));
        tree.add(new TestMessage(3, 0));
        tree.add(new TestMessage(4, 0));

        List<TestMessage> dfsOrder = tree.dfs();
        Assert.assertEquals("0:-1 1:0 2:0 3:0 4:0", Util.list2Str(dfsOrder));
    }

    @Test
    public void testDFS3() {
        Tree<TestMessage> tree = new TreeImpl<TestMessage>(new TestInserter());
        tree.setRoot(new TestMessage(0, -1));
        tree.add(new TestMessage(1, 0));
        tree.add(new TestMessage(2, 1));
        tree.add(new TestMessage(3, 2));
        tree.add(new TestMessage(4, 3));

        List<TestMessage> dfsOrder = tree.dfs();
        Assert.assertEquals("0:-1 1:0 2:1 3:2 4:3", Util.list2Str(dfsOrder));
    }

    @Test
    public void testDFS4() {
        Tree<TestMessage> tree = new TreeImpl<TestMessage>(new TestInserter());
        tree.setRoot(new TestMessage(0, -1));
        tree.add(new TestMessage(1, 0));
        tree.add(new TestMessage(2, 1));
        tree.add(new TestMessage(3, 0));
        tree.add(new TestMessage(4, 2));
        tree.add(new TestMessage(5, 3));
        tree.add(new TestMessage(6, 5));

        List<TestMessage> dfsOrder = tree.dfs();
        Assert.assertEquals("0:-1 1:0 2:1 4:2 3:0 5:3 6:5", Util.list2Str(dfsOrder));
    }

    @Test
    public void testSize() {
        Tree<TestMessage> tree = new TreeImpl<TestMessage>(new TestInserter());
        tree.setRoot(new TestMessage(0, -1));
        tree.add(new TestMessage(1, 0));
        tree.add(new TestMessage(2, 0));
        tree.add(new TestMessage(3, 0));
        tree.add(new TestMessage(4, 1));
        tree.add(new TestMessage(5, 1));
        tree.add(new TestMessage(6, 2));
        tree.add(new TestMessage(7, 2));
        tree.add(new TestMessage(8, 4));
        tree.add(new TestMessage(9, 8));
        tree.add(new TestMessage(10, 9));

        Assert.assertEquals(11, tree.size());
    }

    @Test
    public void testRemoveNode() {
        Tree<TestMessage> tree = new TreeImpl<TestMessage>(new TestInserter());
        tree.setRoot(new TestMessage(0, -1));
        tree.add(new TestMessage(1, 0));
        tree.add(new TestMessage(2, 0));
        tree.add(new TestMessage(3, 0));
        tree.add(new TestMessage(4, 1));
        tree.add(new TestMessage(5, 1));
        tree.add(new TestMessage(6, 2));
        tree.add(new TestMessage(7, 2));
        tree.add(new TestMessage(8, 4));
        tree.add(new TestMessage(9, 8));
        tree.add(new TestMessage(10, 9));

        tree.remove(new TestMessage(5, 1));

        Assert.assertEquals(10, tree.size());

        tree.remove(new TestMessage(1, 0));

        Assert.assertEquals(9, tree.size());
    }
}
