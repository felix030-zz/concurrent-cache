package com.fklemke.concurrentcache.interfaces;

import java.util.List;

/**
 * Interface representing required functionality for parsing text-lines into words.
 */
public interface WordParser {

    List<String> getWordsFromTextLine(String line);
}
