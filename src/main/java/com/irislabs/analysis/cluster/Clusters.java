package com.irislabs.analysis.cluster;


import org.bytedeco.javacpp.opencv_core;

import java.util.Arrays;

/**
 * Author: spartango
 * Date: 10/30/13
 * Time: 5:06 PM.
 */
public class Clusters {
    private final opencv_core.CvMat clusterLabels;
    private final opencv_core.CvMat clusterCenters;
    private final double[]          compactness;

    public Clusters(opencv_core.CvMat clusterLabels,
                    opencv_core.CvMat clusterCenters,
                    double[] compactness) {
        super();
        this.clusterLabels = clusterLabels;
        this.clusterCenters = clusterCenters;
        this.compactness = compactness;
    }

    public opencv_core.CvMat getClusterLabels() {
        return clusterLabels;
    }

    public opencv_core.CvMat getClusterCenters() {
        return clusterCenters;
    }

    public double[] getCompactness() {
        return compactness;
    }

    public int clusterCount() {
        return clusterCenters.rows();
    }

    public int sampleCount() {
        return clusterLabels.size();
    }

    public int getClusterForSample(int index) {
        return (int) clusterLabels.get(index, 0);
    }

    public opencv_core.CvMat getClusterCenter(int clusterIndex) {
        opencv_core.CvMat center = opencv_core.CvMat.createHeader(1,
                                                                  clusterCenters.cols(),
                                                                  opencv_core.CV_32FC1);
        opencv_core.cvGetRow(clusterCenters, center, clusterIndex);
        return center;
    }

    public double getClusterCompactness(int clusterIndex) {
        return compactness[clusterIndex];
    }

    public Cluster getCluster(int label) {
        final opencv_core.CvMat clusterCenter = getClusterCenter(label);
        double[] center = new double[clusterCenter.cols()];
        clusterCenter.get(0, center);
        return new Cluster(label, center, compactness[0]);
    }

    @Override public String toString() {
        return "Clusters{" +
               ", clusterCenters=" + clusterCenters +
               ", compactness=" + Arrays.toString(compactness) +
               '}';
    }
}