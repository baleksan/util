package com.baleksan.util.compression;


import gnu.trove.TIntIntHashMap;

import org.apache.lucene.util.OpenBitSet;

/**
 * Implements the sparse array backed by the BitSet which determines the null (zero) values
 *
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SparseIntArray {
    private TIntIntHashMap hashMap;
    private OpenBitSet zeroValuesBitSet;
    private int maxCapacity;

    public SparseIntArray(int maxCapacity, float loadFactor) {
        this.maxCapacity = maxCapacity;

        hashMap = new TIntIntHashMap((int) (maxCapacity * loadFactor));
        int requiredSize = OpenBitSet.bits2words(maxCapacity);
        long[] initArray = new long[requiredSize];
        for (int i = 0; i < requiredSize; i++) {
            initArray[i] = 0xFFFFFFFFFFFFFFFFL;
        }
        zeroValuesBitSet = new OpenBitSet(initArray, initArray.length);
    }


    public void set(int index, int value) {
        zeroValuesBitSet.ensureCapacity(index + 1);
        if (value != 0) {
            zeroValuesBitSet.fastClear(index);
            hashMap.put(index, value);
        } else {
            zeroValuesBitSet.fastSet(index);
        }
    }

    public int get(int index) {
        if (zeroValuesBitSet.fastGet(index)) {
            return 0;
        } else {
            return hashMap.get(index);
        }
    }

    public int numberNonZeroValues() {
        return hashMap.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}

