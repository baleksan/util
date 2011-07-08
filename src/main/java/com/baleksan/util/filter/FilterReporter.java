package com.baleksan.util.filter;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public interface FilterReporter<K> extends Filter<K> {
    /**
     * Returns the map which maps an explanation (class name of the
     * filter) to the list of features which it filtered.
     *
     * @return the map which maps an explanation (class name of the
     *         filter) to the list of ngrams which it filtered.
     */
    Map<String, List<K>> reportFilteredFeatures();

    int getFilteredCount();

    int getTotalCount();

    void printFilteringReport();

    void printFilteringReport(PrintWriter writer);

    void start();
}
