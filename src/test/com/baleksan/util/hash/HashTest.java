package com.baleksan.util.hash;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class HashTest {
    @Test
    public void testMurmur() {
        Hash murmur = Hash.getInstance(Hash.MURMUR_HASH);

        int value = murmur.hash(1);
        Assert.assertEquals(value, -70065096);

        value = murmur.hash(100);
        Assert.assertEquals(value, 1139144313);

        value = murmur.hash(1000);
        Assert.assertEquals(value, -1567025094);

        value = murmur.hash(1000000);
        Assert.assertEquals(value, 1966964666);

        value = murmur.hash(10000000);
        Assert.assertEquals(value, -34340848);

        value = murmur.hash(50000000);
        Assert.assertEquals(value, 1436385545);

        value = murmur.hash(500000000);
        Assert.assertEquals(value, 965249774);

        value = murmur.hash(50000000000000L);
        Assert.assertEquals(value, 1113593145);
    }
}
