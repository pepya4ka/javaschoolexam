package com.tsystems.javaschool.tasks.subsequence;

import java.util.ArrayList;
import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        // TODO: Implement the logic here
        if (x == null || y == null)
            throw new IllegalArgumentException();
        if (x.size() > y.size()) {
            return false;
        }

        List<Integer> listOfIndexes = new ArrayList<>();
        for (Object temp : x) {
            if (y.contains(temp))
                listOfIndexes.add(y.indexOf(temp));
            else
                return false;
        }

        int[] arrayOfIndexes = new int[listOfIndexes.size()];
        int n = 0;
        for (Object temp : listOfIndexes) {
            arrayOfIndexes[n] = (int) temp;
            n++;
        }

        boolean fl = true;
        for (int i = 0; i < arrayOfIndexes.length - 1; i++) {
            if (arrayOfIndexes[i] > arrayOfIndexes[i + 1]) {
                fl = false;
                break;
            }
        }
        return fl;
    }
}
