package com.baleksan.util.hash;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class JenkinsHashTest {

    @Test
    public void test64bit() {
        JenkinsHash hash = JenkinsHash.get64BitHash();

        Assert.assertEquals(hash.hash64bit("foo".getBytes()), hash.hash64bit("foo".getBytes()));
        Assert.assertEquals(hash.hash64bit("bleepoo".getBytes()), hash.hash64bit("bleepoo".getBytes()));
        Assert.assertFalse(hash.hash64bit("bleepoo".getBytes()) == hash.hash64bit("foopee".getBytes()));
    }
}
