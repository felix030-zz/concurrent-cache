package com.fklemke.concurrentcache.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a Word element destined for Cache entry.
 */
public class Word implements Serializable {

    private String value;
    private int totalOccurrences;
    private Map<String, Integer> occurrenceOrigin;

    public Word() {
    }

    public Word(String value, int totalOccurrences, Map<String, Integer> occurrenceOrigin) {
        this.value = value;
        this.totalOccurrences = totalOccurrences;
        this.occurrenceOrigin = occurrenceOrigin;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTotalOccurrences() {
        return totalOccurrences;
    }

    public void setTotalOccurrences(int totalOccurrences) {
        this.totalOccurrences = totalOccurrences;
    }

    public Map<String, Integer> getOccurrenceOrigin() {
        return occurrenceOrigin;
    }

    public void setOccurrenceOrigin(Map<String, Integer> occurrenceOrigin) {
        this.occurrenceOrigin = occurrenceOrigin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return value.equals(word.value) &&
                occurrenceOrigin.equals(word.occurrenceOrigin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, occurrenceOrigin);
    }

    @Override
    public String toString() {
        return "Word{" +
                "value='" + value + '\'' +
                ", totalOccurrences=" + totalOccurrences +
                ", occurrenceOrigin=" + occurrenceOrigin +
                '}';
    }
}
