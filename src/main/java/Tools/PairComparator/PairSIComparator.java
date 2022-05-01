package Tools.PairComparator;

import javafx.util.Pair;

import java.util.Comparator;

public class PairSIComparator implements Comparator<Pair<String, Integer>> {
    @Override
    public int compare(Pair p1, Pair p2) {
        return -((String) p1.getKey()).compareTo((String) p2.getKey());

    }
}