package Tools.PairComparator;

import javafx.util.Pair;

import java.util.Comparator;

public class PairFIComparator implements Comparator<Pair<Float, Integer>> {
    @Override
    public int compare(Pair p1, Pair p2) {
        return -(((Float) p1.getKey()).compareTo((Float) p2.getKey()));
    }
}
