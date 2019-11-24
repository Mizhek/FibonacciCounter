package com.example.fibonnacicounter;

public class MathUtils {

    public static Long calcRecursion(int num) {
        if (num == 0) {
            return 0L;
        } else if (num < 3) {
            return 1L;
        } else {
            return calcRecursion(num - 1)
                    + calcRecursion(num - 2);
        }
    }

    public static Long calculateArray(int num) {

        long[] results = new long[num + 2];

        for (int i = 0; i <= num; i++) {

            if (i == 0) {
                results[i] = 0;
                continue;
            }
            if (i == 1) {
                results[i] = 1;
                continue;
            }

            results[i] = results[i - 1] + results[i - 2];

        }

        return results[num];

    }

    public static Long calcBinetFormula(Integer num) {
        double phi = (1 + Math.sqrt(5)) / 2;
        return Math.round(Math.pow(phi, num) / Math.sqrt(5));
    }
}
