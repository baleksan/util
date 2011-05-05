package com.baleksan.util.collections;

import com.baleksan.util.Util;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class ForestTest {

    @Test
    public void testSimpleForest() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(0, -1));
        forest.add(new TestMessage(1, 0));
        forest.add(new TestMessage(2, 0));
        forest.add(new TestMessage(3, 0));
        forest.add(new TestMessage(4, 1));
        forest.add(new TestMessage(5, 1));
        forest.add(new TestMessage(6, 2));
        forest.add(new TestMessage(7, 2));
        forest.add(new TestMessage(8, 4));
        forest.add(new TestMessage(9, 8));
        forest.add(new TestMessage(10, 9));

        Assert.assertEquals(forest.getTrees().size(), 1);
        Assert.assertEquals(forest.getTrees().get(0).size(), 11);
        Assert.assertEquals("0:-1 1:0 4:1 8:4 9:8 10:9 5:1 2:0 6:2 7:2 3:0", Util.list2Str(forest.getTrees().get(0).dfs()));
    }

    @Test
    public void testForestMultipleTrees() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(0, -1));
        forest.add(new TestMessage(1, 0));
        forest.add(new TestMessage(7, 5));
        forest.add(new TestMessage(8, 1));

        Assert.assertEquals(forest.getTrees().size(), 2);
        Assert.assertEquals(forest.getTrees().get(0).size(), 3);
        Assert.assertEquals(forest.getTrees().get(1).size(), 1);

        Assert.assertEquals("0:-1 1:0 8:1", Util.list2Str(forest.getTrees().get(0).dfs()));
    }

    @Test
    public void testMergeTrees() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(0, -1));
        forest.add(new TestMessage(1, 0));
        forest.add(new TestMessage(7, 5));
        forest.add(new TestMessage(8, 1));
        forest.add(new TestMessage(5, 8));

        Assert.assertEquals(forest.getTrees().size(), 2);
        Assert.assertEquals(forest.getTrees().get(0).size(), 4);
        Assert.assertEquals(forest.getTrees().get(1).size(), 1);

        Assert.assertEquals("0:-1 1:0 8:1 5:8", Util.list2Str(forest.getTrees().get(0).dfs()));

        forest.mergeTrees();

        Assert.assertEquals(forest.getTrees().size(), 1);
        Assert.assertEquals(forest.getTrees().get(0).size(), 5);
        Assert.assertEquals("0:-1 1:0 8:1 5:8 7:5", Util.list2Str(forest.getTrees().get(0).dfs()));
    }

    @Test
    public void testMergeTrees2() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(0, -1));
        forest.add(new TestMessage(7, 5));
        forest.add(new TestMessage(8, 1));
        forest.add(new TestMessage(1, 0));
        forest.add(new TestMessage(5, 8));

        Assert.assertEquals(forest.getTrees().size(), 3);
        Assert.assertEquals(forest.getTrees().get(0).size(), 2);
        Assert.assertEquals(forest.getTrees().get(1).size(), 1);
        Assert.assertEquals(forest.getTrees().get(2).size(), 2);

        Assert.assertEquals("0:-1 1:0", Util.list2Str(forest.getTrees().get(0).dfs()));
        Assert.assertEquals("7:5", Util.list2Str(forest.getTrees().get(1).dfs()));
        Assert.assertEquals("8:1 5:8", Util.list2Str(forest.getTrees().get(2).dfs()));

        forest.mergeTrees();

        Assert.assertEquals(forest.getTrees().size(), 1);
        Assert.assertEquals(forest.getTrees().get(0).size(), 4);
        Assert.assertEquals("0:-1 1:0 8:1 5:8", Util.list2Str(forest.getTrees().get(0).dfs()));
    }

    @Test
    public void testMergeTreesSymmetricalMerge() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(1, 0));
        forest.add(new TestMessage(2, 1));
        forest.add(new TestMessage(3, 4));
        forest.add(new TestMessage(4, 2));
        forest.add(new TestMessage(5, 0));

        forest.mergeTrees();
        Assert.assertEquals(forest.getTrees().size(), 2);
    }

    @Test
    public void testMergeTrees3() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(0, -1));
        forest.add(new TestMessage(2, 1));
        forest.add(new TestMessage(1, -1));
        forest.add(new TestMessage(3, 0));
        forest.add(new TestMessage(4, 8));
        forest.add(new TestMessage(45, 8));
        forest.add(new TestMessage(8, 2));

        List<Tree<TestMessage>> trees = forest.getTrees();
        Assert.assertEquals(trees.size(), 5);
        Assert.assertEquals(trees.get(0).size(), 2);
        Assert.assertEquals(trees.get(1).size(), 1);
        Assert.assertEquals(trees.get(2).size(), 2);
        Assert.assertEquals(trees.get(3).size(), 1);
        Assert.assertEquals(trees.get(4).size(), 1);

        Assert.assertEquals("0:-1 3:0", Util.list2Str(forest.getTrees().get(0).dfs()));
        Assert.assertEquals("1:-1", Util.list2Str(forest.getTrees().get(1).dfs()));
        Assert.assertEquals("2:1 8:2", Util.list2Str(forest.getTrees().get(2).dfs()));
        Assert.assertEquals("4:8", Util.list2Str(forest.getTrees().get(3).dfs()));
        Assert.assertEquals("45:8", Util.list2Str(forest.getTrees().get(4).dfs()));

        Assert.assertTrue(trees.get(2).canMergeWith(trees.get(3)));

        forest.mergeTrees();

        Assert.assertEquals(forest.getTrees().size(), 2);
        Assert.assertEquals(forest.getTrees().get(0).size(), 2);
        Assert.assertEquals(forest.getTrees().get(1).size(), 5);

        Assert.assertEquals("0:-1 3:0", Util.list2Str(forest.getTrees().get(0).dfs()));
        Assert.assertEquals("1:-1 2:1 8:2 45:8 4:8", Util.list2Str(forest.getTrees().get(1).dfs()));
    }

    @Test
    public void testDfs() {
        Forest<TestMessage> forest = new ForestImpl<TestMessage>(new TestInserter());
        forest.add(new TestMessage(0, -1));
        forest.add(new TestMessage(2, 1));
        forest.add(new TestMessage(1, -1));
        forest.add(new TestMessage(3, 0));
        forest.add(new TestMessage(4, 8));
        forest.add(new TestMessage(45, 8));
        forest.add(new TestMessage(8, 2));

        List<Tree<TestMessage>> trees = forest.getTrees();
        Assert.assertEquals(trees.size(), 5);
        Assert.assertEquals(trees.get(0).size(), 2);
        Assert.assertEquals(trees.get(1).size(), 1);
        Assert.assertEquals(trees.get(2).size(), 2);
        Assert.assertEquals(trees.get(3).size(), 1);
        Assert.assertEquals(trees.get(4).size(), 1);

        Assert.assertEquals("0:-1 3:0", Util.list2Str(forest.getTrees().get(0).dfs()));
        Assert.assertEquals("1:-1", Util.list2Str(forest.getTrees().get(1).dfs()));
        Assert.assertEquals("2:1 8:2", Util.list2Str(forest.getTrees().get(2).dfs()));
        Assert.assertEquals("4:8", Util.list2Str(forest.getTrees().get(3).dfs()));
        Assert.assertEquals("45:8", Util.list2Str(forest.getTrees().get(4).dfs()));

        Assert.assertTrue(trees.get(2).canMergeWith(trees.get(3)));

        forest.mergeTrees();

        Assert.assertEquals(forest.getTrees().size(), 2);
        Assert.assertEquals(forest.getTrees().get(0).size(), 2);
        Assert.assertEquals(forest.getTrees().get(1).size(), 5);

        Assert.assertEquals("0:-1 3:0 1:-1 2:1 8:2 45:8 4:8", Util.list2Str(forest.dfs()));
    }
}
