package dictionaryManager;

import java.util.Comparator;

public class CompareWords implements Comparator<Words> {
    @Override
    public int compare(Words w1, Words w2) { return w1.getWord().compareTo(w2.getWord()); }
}
