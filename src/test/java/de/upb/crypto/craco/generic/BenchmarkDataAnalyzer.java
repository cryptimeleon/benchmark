package de.upb.crypto.craco.generic;

public class BenchmarkDataAnalyzer {
    /**
     * Computes the average of a one-dimensional array of long values.
     */
    public static long compute1DAverage(Long[] data) {
        long avg = 0;
        for (Long time : data) {
            avg += time;
        }
        return avg / data.length;
    }

    /**
     * Computes the average of a two-dimensional array of long values.
     */
    public static long compute2DAverage(Long[][] data) {
        Long[] subAvgs = new Long[data.length];
        for (int i = 0; i < data.length; ++i) {
            subAvgs[i] = compute1DAverage(data[i]);
        }
        return compute1DAverage(subAvgs);
    }

    /**
     * Computes the average of a three-dimensional array of long values.
     */
    public static long compute3DAverage(Long[][][] data) {
        Long[] subAvgs = new Long[data.length];
        for (int i = 0 ; i < data.length; ++i) {
            subAvgs[i] = compute2DAverage(data[i]);
        }
        return compute1DAverage(subAvgs);
    }
}
