package com.fklemke.concurrentcache.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class WordParseServiceTest {

    private static final String TEST_LINE_1 = "Hear me, he cried, O god of the silver bow,that protectest Chryse and " +
            "holy Cilla and rulest Tenedos with thy might, hear me oh thou of Sminthe.";
    private static final int TEST_LINE_1_WORD_COUNT = 28;
    private static final String TEST_LINE_2 = "וואלה! תקשורת בע\"מ היא חברת תקשורת ישראלית פרטית מקבוצת בזק, המפעילה את פורטל וואלה!";
    private static final int TEST_LINE_2_WORD_COUNT = 15;

    private WordParseService wordParseService;
    private List<String> words;


    @BeforeEach
    void setUp() {
        wordParseService = new WordParseService();
        words = new ArrayList<>();
    }

    @Test
    @DisplayName("Test on: English text line parsed gives correct word count.")
    void testGetWordsFromTextLine_TL1() {
        //given TEST_LINE_1

        whenGetWordsFromTextLineCalled(TEST_LINE_1);

        thenTotalWordCountCorrect(TEST_LINE_1_WORD_COUNT);
    }

    @Test
    @DisplayName("Test on: Hebrew text linecle parsed gives correct word count.")
    void testGetWordsFromTextLine_TL2() {
        //given TEST_LINE_2

        whenGetWordsFromTextLineCalled(TEST_LINE_2);

        thenTotalWordCountCorrect(TEST_LINE_2_WORD_COUNT);
    }

    //WHEN
    private void whenGetWordsFromTextLineCalled(String line) {
        words = wordParseService.getWordsFromTextLine(line);
    }

    //THEN
    private void thenTotalWordCountCorrect(int wordCount) {
        assertSame(words.size(), wordCount);
    }
}
