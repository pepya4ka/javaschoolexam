package com.tsystems.javaschool.tasks.pyramid;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here

        if (inputNumbers.size() > 254)
            throw new CannotBuildPyramidException();
        if (inputNumbers.contains(null))
            throw new CannotBuildPyramidException();
        int i = 0;
        int[] numbers;
            numbers = new int[inputNumbers.size()];
            for (int j : inputNumbers) {
                numbers[i] = (int) j;
                i++;
            }


        Arrays.sort(numbers);

        i = 0;
        int j = 0;
        int p = 0;
        boolean fl = false;
        while (true) {
            if (i + (j + 1) == numbers.length) {
                j++;
                break;
            }
            if (i + (j + 1) > numbers.length) {
                p = numbers[numbers.length - 1] - i;
                j++;
                fl = true;
                break;
            }
            j++;
            i += j;
        }

        return generatePyramid(j, j * 2 - 1, numbers, fl, p);
    }

    public int[][] generatePyramid(int x, int y, int[] numbers, boolean fl, int p) {
        int[][] pyramidOfNumbers = new int[x][y];

        int n = 0;
        int k = y / 2 + 1;
        for (int i = 0; i < x; i++) {
            if (i == x - 1 && fl) {
                for (int j = y / 2 + 1 - p, m = 0; m < p; j += 2, m++) {
                    pyramidOfNumbers[i][j] = numbers[n];
                    n++;
                }
            } else {
                for (int j = k - 1, m = 0; m < i + 1; j += 2, m++) {
                    pyramidOfNumbers[i][j] = numbers[n];
                    n++;
                }
            }
            k--;
        }

        return pyramidOfNumbers;
    }


}
