package com.fklemke.concurrentcache.domain;

import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to atomically mutate Cache data.
 */
public class WordEntryProcessor implements EntryProcessor<String, Word, Word>, Serializable {

    private static final int VALUE_FIRST_ENTRY = 1;
    private static final int INCREMENT_BY_ONE = 1;

    /**
     * Processes a given entry against a Cache. Method is atomic and will lock the Cache key.
     *
     * @param entry the entry to be added or mutated.
     * @param arguments index[0] represents the filename where the entry originates.
     * @return the Word object that was added or mutated.
     * @throws EntryProcessorException indicate a problem occurred attempting to execute against an entry.
     */
    @Override
    public Word process(MutableEntry<String, Word> entry, Object... arguments) throws EntryProcessorException {
        String originFileName = String.valueOf(arguments[0]);

        Word wordEntry;
        if(entry.exists()) {
            //UPDATE ENTRY
            wordEntry = entry.getValue();

            if(wordEntry.getOccurrenceOrigin().containsKey(originFileName)) {
                //UPDATE WORD COUNT FOR ORIGIN FILE
                Integer currentValue = wordEntry.getOccurrenceOrigin().get(originFileName);
                wordEntry.getOccurrenceOrigin().put(originFileName, currentValue + INCREMENT_BY_ONE);

                int updatedTotalOccurrence = Math.toIntExact(getTotalWordOccurrence(wordEntry.getOccurrenceOrigin()));
                wordEntry.setTotalOccurrences(updatedTotalOccurrence);
                entry.setValue(wordEntry);
            } else {
                //ADD AS NEW ENTRY FOR ORIGIN FILE
                wordEntry.getOccurrenceOrigin().put(originFileName, VALUE_FIRST_ENTRY);
                int updatedTotalOccurrence = Math.toIntExact(getTotalWordOccurrence(wordEntry.getOccurrenceOrigin()));
                wordEntry.setTotalOccurrences(updatedTotalOccurrence);
                entry.setValue(wordEntry);
            }
        } else {
            //ADD AS NEW ENTRY
            Map<String, Integer> firstOccurrence = new HashMap<>();
            firstOccurrence.put(originFileName, VALUE_FIRST_ENTRY);
            wordEntry = new Word(entry.getKey(), VALUE_FIRST_ENTRY, firstOccurrence);
            wordEntry.setTotalOccurrences(VALUE_FIRST_ENTRY);
            entry.setValue(wordEntry);
        }
        return wordEntry;
    }

    private long getTotalWordOccurrence(Map<String, Integer> wordOccurrencesInFiles) {
        return wordOccurrencesInFiles
                .entrySet()
                .stream()
                .mapToInt(Map.Entry::getValue)
                .sum();
    }
}
