package com.baleksan.util.random;

import java.util.Random;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class RandomUtils {
    private Random random;

    public RandomUtils() {
        random = new Random();
    }

    public int randomInt(int min, int max) {
        int nextRandom = Math.abs(random.nextInt());
        int result = nextRandom > max ? nextRandom % max : nextRandom;

        if (result < min) {
            return min;
        }

        return result;
    }

    public long randomLong(long min, long max) {
        long nextRandom = Math.abs(random.nextLong());
        long result = nextRandom > max ? nextRandom % max : nextRandom;

        if (result < min) {
            return min;
        }

        return result;
    }
}
