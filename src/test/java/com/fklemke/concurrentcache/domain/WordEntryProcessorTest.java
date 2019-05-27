package com.fklemke.concurrentcache.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.cache.processor.MutableEntry;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordEntryProcessorTest {

    private static final String WORD_EXP_1 = "Test";
    private static final String TEST_FILE_1 = "test-file-1.txt";
    private static final String TEST_FILE_2 = "test-file-2.txt";

    private String testWordOriginFile;

    private WordEntryProcessor wordEntryProcessor;
    private Word wordEntry;

    @Mock
    private MutableEntry<String, Word> entry;

    @BeforeEach
    void setUp() {
        wordEntryProcessor = new WordEntryProcessor();
    }

    @Test
    @DisplayName("Test on: Adding a new Word object to the Cache.")
    void testProcess_AddNewWord() {
        givenNewWordProcessRequest(TEST_FILE_1, WORD_EXP_1);

        whenProcessCalled();

        thenNewWordAdded();
    }

    @Test
    @DisplayName("Test on: Adding an occurrence to an existing Word object, found in the same file.")
    void testProcess_AddNewEntryFromDifferentOrigin() {
        givenExistingEntry(TEST_FILE_1, WORD_EXP_1);
        givenEntryFormOrigin(TEST_FILE_2);

        whenProcessCalled();

        thenWordCountUpdated_OnNewOriginFile();
    }

    @Test
    @DisplayName("Test on: Adding an occurrence to an existing Word object, found in the same file.")
    void testProcess_AddEntryToOrigin() {
        givenExistingEntry(TEST_FILE_1, WORD_EXP_1);
        givenEntryFormOrigin(TEST_FILE_1);

        whenProcessCalled();

        thenWordCountUpdated_OnOriginFile();
    }

    //GIVEN
    private void givenExistingEntry(String originFile, String word) {
        Map<String, Integer> occurrenceOrigin = new HashMap<>();
        occurrenceOrigin.put(originFile, 1);
        wordEntry = new Word(word, 1, occurrenceOrigin);

        when(entry.exists()).thenReturn(true);
        when(entry.getValue()).thenReturn(wordEntry);
    }
    private void givenEntryFormOrigin(String filename) {
        testWordOriginFile = filename;
    }
    private void givenNewWordProcessRequest(String filename, String word) {
        testWordOriginFile = filename;
        when(entry.getKey()).thenReturn(word);
    }

    //WHEN
    private void whenProcessCalled() {
        wordEntry = wordEntryProcessor.process(entry, testWordOriginFile);
    }

    //THEN
    private void thenNewWordAdded() {
        assertEquals(wordEntry.getValue(), WORD_EXP_1);
        assertSame(1, wordEntry.getTotalOccurrences());
        assertTrue(wordEntry.getOccurrenceOrigin().containsKey(testWordOriginFile));
    }
    private void thenWordCountUpdated_OnOriginFile() {
        assertEquals(wordEntry.getValue(), WORD_EXP_1);
        assertSame(2, wordEntry.getTotalOccurrences());
        assertSame(2, wordEntry.getOccurrenceOrigin().get(TEST_FILE_1));
        assertTrue(wordEntry.getOccurrenceOrigin().containsKey(TEST_FILE_1));
        assertFalse(wordEntry.getOccurrenceOrigin().containsKey(TEST_FILE_2));
    }
    private void thenWordCountUpdated_OnNewOriginFile() {
        assertEquals(wordEntry.getValue(), WORD_EXP_1);
        assertSame(2, wordEntry.getTotalOccurrences());
        assertTrue(wordEntry.getOccurrenceOrigin().containsKey(TEST_FILE_2));
        assertTrue(wordEntry.getOccurrenceOrigin().containsKey(TEST_FILE_1));
    }
}