package com.baleksan.util;

import java.util.IdentityHashMap;
import java.util.Map;

import com.baleksan.util.compression.SparseIntArray;
import org.apache.lucene.util.OpenBitSet;


/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */

public class MemorySizer {
    private static final Map<Class, Integer> primitiveSizes;

    static {
        primitiveSizes = new IdentityHashMap<Class, Integer>();

        primitiveSizes.put(boolean.class, 1);
        primitiveSizes.put(byte.class, 1);
        primitiveSizes.put(char.class, 2);
        primitiveSizes.put(short.class, 2);
        primitiveSizes.put(int.class, 4);
        primitiveSizes.put(float.class, 4);
        primitiveSizes.put(double.class, 8);
        primitiveSizes.put(long.class, 8);
    }

    public static int getMemorySize(Class clazz) {
        return primitiveSizes.get(clazz);
    }

    public static float estimateArraySize(boolean[] array) {
        return (float) (array.length * primitiveSizes.get(boolean.class)) / 1024l / 1024l;
    }

    public static float estimateArraySize(int[] array) {
        return (float) (array.length * primitiveSizes.get(int.class)) / 1024l / 1024l;
    }

    public static float estimateArraySize(short[] array) {
        return (float) (array.length * primitiveSizes.get(short.class)) / 1024l / 1024l;
    }

    public static float estimateArraySize(long[] array) {
        return (float) (array.length * primitiveSizes.get(long.class)) / 1024l / 1024l;
    }

    public static float estimateArraySize(String[] array, long totalChars) {
        return ((float) (totalChars * 2)) / 1024l / 1024l;
    }

    public static float estimateArraySize(String[] array, int averageCharsPerEntry) {
        return ((float) (array.length * averageCharsPerEntry * 2)) / 1024l / 1024l;
    }

    public static float estimateArraySize(SparseIntArray array) {
        int bitSetSize = OpenBitSet.bits2words(array.getMaxCapacity()) * primitiveSizes.get(long.class);
        int mapSize = array.numberNonZeroValues() * primitiveSizes.get(int.class) * 2;

        return (bitSetSize + mapSize)  / 1024l / 1024l;
    }


}

