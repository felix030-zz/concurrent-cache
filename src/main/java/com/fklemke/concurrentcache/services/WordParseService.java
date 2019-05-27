package com.fklemke.concurrentcache.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle parsing into native words.
 */
@Service
public class WordParseService {

    /**
     * Handles parsing of single lines of words into a List of lowercase words.
     * @param line the given text line.
     * @return a List of lowercase words.
     */
    List<String> getWordsFromTextLine(String line) {
        List<String> resultWordList = new ArrayList<>();

        //\p{L} or \p{Letter}: matches on any kind of letter from any language.
        String[] wordArr = line.split("\\P{L}");
        for (String word : wordArr) {
            if (!word.isEmpty()) {
                resultWordList.add(word.toLowerCase());
            }
        }
        return resultWordList;
    }
}
