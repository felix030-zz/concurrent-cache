package com.fklemke.concurrentcache.services;

import com.fklemke.concurrentcache.domain.Word;
import com.fklemke.concurrentcache.domain.WordComparatorByTotalOccurrence;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

/**
 * Service class to handle to print out of the word statistics.
 */
@Service
public class WordStatisticsPrintOutService {

    private Cache<String, Word> cache;

    public WordStatisticsPrintOutService(Cache<String, Word> cache) {
        this.cache = cache;
    }

    /**
     * Prints out the entire cache sorted by total occurrence.
     */
    @Async
    public void printOutCache() {
        Iterator<Cache.Entry<String,Word>> allCacheEntries = cache.iterator();
        List<Word> words = new ArrayList<>();

        while(allCacheEntries.hasNext()){
            Cache.Entry<String,Word> currentEntry = allCacheEntries.next();
            words.add(currentEntry.getValue());
        }

        words.sort(new WordComparatorByTotalOccurrence());

        words.forEach( w -> {
            StringJoiner sj = new StringJoiner(" + ");
            w.getOccurrenceOrigin().forEach((k,v) -> {
                sj.add("<" + v + " in file " + k + ">");
            });

            System.out.println(String.format("<%s> <%d> = %s", w.getValue(), w.getTotalOccurrences(), sj.toString()));
        });
    }
}
