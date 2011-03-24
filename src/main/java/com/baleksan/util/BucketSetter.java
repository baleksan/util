package com.baleksan.util;

import com.flaptor.hist4j.AdaptiveHistogram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class BucketSetter {

    public static void setBuckets(List<? extends Scorable> items, int numBins, int center) {
        if (numBins <= 1) {
            throw new IllegalArgumentException("Number of bins must be greater than one.");
        }

        if (items == null || items.size() <= 0) {
            return;
        }

        AdaptiveHistogram hist = new AdaptiveHistogram();
        Set<Float> uniqueValues = new HashSet<Float>();
        float base = 1.5f;
        for (Scorable item : items) {
            float score = item.getScore();
            if (!uniqueValues.contains(score)) {
                hist.addValue((float) Math.log(score) / (float) Math.log(base));
            }
            uniqueValues.add(score);
        }
        int numValues = uniqueValues.size();
        if (numValues < numBins) {
            numBins = numValues;
        }

        int width = 100 / numBins;
        List<Float> cuts = new ArrayList<Float>();
        for (int i = 1; i < numBins; i++) {
            float cut = hist.getValueForPercentile(width * i);
            cuts.add((float) Math.pow(base, cut));
        }

        int min = (int) Math.ceil(center - numBins / 2.0);
        for (Scorable item : items) {
            item.setBucket(min + numBins - 1);
            for (int i = 0; i < numBins - 1; i++) {
                if (item.getScore() < cuts.get(i)) {
                    item.setBucket(i + min);
                    break;
                }
            }
        }
    }
}
