package com.irislabs.analysis.pca;

import static org.bytedeco.javacpp.opencv_core.*;

/**
 * Author: spartango
 * Date: 6/21/14
 * Time: 11:54.
 */
public class PrincipalComponents {
    private final int components;

    private final CvMat eigenValues;
    private final CvMat eigenVectors;
    private final CvMat subSpace;
    private final CvMat averages;

    public PrincipalComponents(int components,
                               CvMat eigenValues,
                               CvMat eigenVectors,
                               CvMat subSpace,
                               CvMat averages) {
        this.components = components;
        this.eigenValues = eigenValues;
        this.eigenVectors = eigenVectors;
        this.subSpace = subSpace;
        this.averages = averages;
    }

    public CvMat getEigenValues() {
        return eigenValues;
    }

    public CvMat getEigenVectors() {
        return eigenVectors;
    }

    public CvMat getSubSpace() {
        return subSpace;
    }

    public CvMat getAverages() {
        return averages;
    }

    public double getEigenValue(int componentIndex) {
        return eigenValues.get(componentIndex, 0);
    }

    public CvMat getEigenVector(int componentIndex) {
        CvMat eigenVector = CvMat.createHeader(1,
                                               eigenVectors.cols(),
                                               CV_32FC1);
        cvGetRow(eigenVectors, eigenVector, componentIndex);
        return eigenVector;
    }

    public double getAverage(int componentIndex) {
        return averages.get(componentIndex, 0);
    }

    public int componentCount() {
        return eigenVectors.rows();
    }
}
