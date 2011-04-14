package com.baleksan.util;

import com.baleksan.util.random.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class RandomUtilsTest {
    @Test
    public void testRandom() {
        RandomUtils random = new RandomUtils();
        rangeCheck(random, 9, 22);
        rangeCheck(random, 9000, 9001);
        rangeCheck(random, 90, 122);
        rangeCheck(random, 900, 1122);
        rangeCheck(random, 0, 20);
    }

    private void rangeCheck(RandomUtils random, int min, int max) {
        int result = random.randomInt(min, max);
        Assert.assertTrue(result >= min);
        Assert.assertTrue(result <= max);
    }
}
