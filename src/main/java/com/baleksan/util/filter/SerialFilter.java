package com.baleksan.util.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SerialFilter<K> implements FilterReporter<K> {
    private static Logger LOG = LogManager.getLogger(SerialFilter.class);

    Filter<K>[] filters;
    Map<String, List<K>> explanationFeatureListMap;
    int filteredCount;

    public SerialFilter(Filter<K>... filters) {
        this.filters = filters;
        explanationFeatureListMap = new HashMap<String, List<K>>();
    }

    @Override
    public boolean accept(K key) {
        for (Filter<K> filter : filters) {
            //one need to fail for all to fail
            if (!filter.accept(key)) {
                //prepare an explanation
                prepareExplanation(filter, key);

                return false;
            }
        }

        return true;
    }

    private void prepareExplanation(Filter filter, K key) {
        String explanation = filter.getClass().getName();

        List<K> featureList = explanationFeatureListMap.containsKey(explanation)
                ? explanationFeatureListMap.get(explanation)
                : new ArrayList<K>();
        featureList.add(key);

        explanationFeatureListMap.put(explanation, featureList);

        filteredCount++;
    }

    @Override
    public Map<String, List<K>> reportFilteredFeatures() {
        return explanationFeatureListMap;
    }

    @Override
    public int getFilteredCount() {
        return filteredCount;
    }

    @Override
    public void printFilteringReport(int count) {
        int allPotentialCounts = count + getFilteredCount();
        LOG.debug("Filtered out " + getFilteredCount() + " keys out of " + allPotentialCounts + " = " +
                (float) getFilteredCount() / (float) allPotentialCounts * 100 + " %. Left " +
                (allPotentialCounts - getFilteredCount()));

        Map<String, List<K>> explanationFeatureListMap = reportFilteredFeatures();
        for (String explanation : explanationFeatureListMap.keySet()) {
            int size = explanationFeatureListMap.get(explanation).size();
            LOG.debug("Filter " + explanation + " filtered out " + size + " keys " +
                    (float) size / (float) getFilteredCount() * 100 + "%");
        }
    }
}


