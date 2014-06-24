package com.irislabs.analysis.pca;

import org.bytedeco.javacpp.opencv_core;

import java.util.Collection;

import static org.bytedeco.javacpp.opencv_core.CvMat;

/**
 * Author: spartango
 * Date: 6/21/14
 * Time: 11:47.
 */
public class PrincipalComponentAnalyzer {

    private final int principalComponentCount;

    public PrincipalComponentAnalyzer(int principalComponentCount) {
        this.principalComponentCount = principalComponentCount;
    }

    public PrincipalComponents analyze(Collection<double[]> data) {
        if (!data.isEmpty()) {
            // Each row is an image, each column is a pixel
            int dimensons = data.stream().findAny().get().length;
            final CvMat matrix = CvMat.create(data.size(), dimensons, opencv_core.CV_32FC1);
            // Copy the data
            int sampleIndex = 0;
            for (double[] dataPoint : data) {
                for (int i = 0; i < dataPoint.length; i++) {
                    matrix.put(sampleIndex, i, dataPoint[i]);
                }
                sampleIndex++;
            }

            // Run PCA on the target matrix
            CvMat eigenValues = CvMat.create(principalComponentCount, 1);
            CvMat eigenVectors = CvMat.create(principalComponentCount,
                                              dimensons,
                                              opencv_core.CV_32FC1);
            CvMat averages = CvMat.create(1, dimensons, opencv_core.CV_32FC1);

            opencv_core.cvCalcPCA(matrix,
                                  averages,
                                  eigenValues,
                                  eigenVectors,
                                  opencv_core.CV_PCA_DATA_AS_ROW);

            CvMat subspace = CvMat.create(data.size(),
                                          principalComponentCount,
                                          opencv_core.CV_32FC1);

            opencv_core.cvProjectPCA(matrix,
                                     averages,
                                     eigenVectors,
                                     subspace);

            // Stuff everything into the principal components
            PrincipalComponents components = new PrincipalComponents(principalComponentCount,
                                                                     eigenValues,
                                                                     eigenVectors,
                                                                     subspace,
                                                                     averages);

            return components;
        } else {
            return null;
        }
    }
}
