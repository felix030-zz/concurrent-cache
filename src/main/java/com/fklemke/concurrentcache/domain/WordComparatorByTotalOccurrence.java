package com.fklemke.concurrentcache.domain;

import java.util.Comparator;

/**
 * Comparator to sort on total word occurrence.
 */
public class WordComparatorByTotalOccurrence implements Comparator<Word> {

    @Override
    public int compare(Word word1, Word word2) {
        return word2.getTotalOccurrences() - word1.getTotalOccurrences();
    }
}
