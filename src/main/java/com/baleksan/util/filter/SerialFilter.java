package com.baleksan.util.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class SerialFilter<K> implements FilterReporter<K> {
    private static Logger LOG = LogManager.getLogger(SerialFilter.class);

    protected Filter<K>[] filters;
    Map<String, List<K>> explanationFeatureListMap;
    int filteredCount;
    protected int totalCount;

    public SerialFilter(Filter<K>... filters) {
        this.filters = filters;
        explanationFeatureListMap = new HashMap<String, List<K>>();
    }

    @Override
    public void start() {
        totalCount = 0;
    }

    @Override
    public boolean accept(K key) {
        totalCount++;

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

    protected void prepareExplanation(Filter filter, K key) {
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
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public void printFilteringReport() {
        int allPotentialCounts = totalCount + getFilteredCount();
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

    @Override
    public void printFilteringReport(PrintWriter writer) {
        int allPotentialCounts = totalCount + getFilteredCount();

        writer.write("Filtered out " + getFilteredCount() + " keys out of " + allPotentialCounts + " = " +
                (float) getFilteredCount() / (float) allPotentialCounts * 100 + " %. Left " +
                (allPotentialCounts - getFilteredCount()));

        Map<String, List<K>> explanationFeatureListMap = reportFilteredFeatures();
        for (String explanation : explanationFeatureListMap.keySet()) {
            int size = explanationFeatureListMap.get(explanation).size();
            writer.write("Filter " + explanation + " filtered out " + size + " keys " +
                    (float) size / (float) getFilteredCount() * 100 + "%");
        }
    }
}


