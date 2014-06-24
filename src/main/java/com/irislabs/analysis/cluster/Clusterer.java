package com.irislabs.analysis.cluster;


import org.bytedeco.javacpp.opencv_core;

import java.util.Collection;

/**
 * Author: spartango
 * Date: 10/30/13
 * Time: 4:58 PM.
 */
public class Clusterer {

    private static final int ATTEMPTS = 1;
    private static final int FLAGS    = 0;
    private int clusters;
    private int    iterations = 100;
    private double epsilon    = 1e-2;

    public Clusterer(int clusters) {
        this.clusters = clusters;
    }

    public Clusterer(int clusters, int iterations, double epsilon) {
        this.clusters = clusters;
        this.iterations = iterations;
        this.epsilon = epsilon;
    }

    public Clusters cluster(Collection<double[]> data) {
        int dimensons = data.stream().findAny().get().length;
        final opencv_core.CvMat matrix = opencv_core.CvMat.create(data.size(), dimensons, opencv_core.CV_32FC1);
        // Copy the data
        int sampleIndex = 0;
        for (double[] dataPoint : data) {
            for (int i = 0; i < dataPoint.length; i++) {
                matrix.put(sampleIndex, i, dataPoint[i]);
            }
            sampleIndex++;
        }
        return cluster(matrix);
    }

    public Clusters cluster(opencv_core.CvMat data) {
        opencv_core.CvTermCriteria terminationCriteria = opencv_core.cvTermCriteria(opencv_core.CV_TERMCRIT_EPS
                                                                                    + opencv_core.CV_TERMCRIT_ITER,
                                                                                    iterations,
                                                                                    epsilon
        );

        opencv_core.CvMat clusterLabels = opencv_core.CvMat.create(data.rows(), 1, opencv_core.CV_32SC1);
        opencv_core.CvMat clusterCenters = opencv_core.CvMat.create(clusters,
                                                                    data.cols(),
                                                                    opencv_core.CV_32FC1);
        double[] compactness = new double[1];
        long[] rng = new long[1];

        opencv_core.cvKMeans2(data,
                              clusters,
                              clusterLabels,
                              terminationCriteria,
                              ATTEMPTS,
                              rng,
                              FLAGS,
                              clusterCenters,
                              compactness);

        compactness[0] = Math.sqrt(compactness[0] / data.rows());
        return new Clusters(clusterLabels, clusterCenters, compactness);
    }
}

