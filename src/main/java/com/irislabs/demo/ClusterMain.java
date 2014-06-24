package com.irislabs.demo;

import com.irislabs.analysis.cluster.Clusterer;
import com.irislabs.analysis.cluster.Clusters;
import com.irislabs.analysis.pca.PrincipalComponentAnalyzer;
import com.irislabs.analysis.pca.PrincipalComponents;
import com.irislabs.sheet.FileSheet;
import com.irislabs.sheet.SheetEntry;
import com.irislabs.sheet.SheetWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Author: spartango
 * Date: 6/20/14
 * Time: 16:22.
 */
public class ClusterMain {

    public static final  int COMPONENTS = 2;
    private static final int CLUSTERS   = 4;

    public static void main(String[] args) throws IOException {

        FileSheet patients = new FileSheet("dcis/numeric.txt");
        System.out.println(patients.fields());

        final List<double[]> numeric = patients.stream().map(patient -> {
            double[] data = new double[patients.fields().size()];
            int i = 0;
            for (String field : patients.fields()) {
                try {
                    data[i] = patient.getDouble(field);
                } catch (NumberFormatException e) {
                    data[i] = 0;
                }
                i++;
            }
            return data;
        }).collect(Collectors.toList());

        Clusterer clusterer = new Clusterer(CLUSTERS, 10000, 1e-5);
        PrincipalComponentAnalyzer pca = new PrincipalComponentAnalyzer(COMPONENTS);
        final PrincipalComponents principalComponents = pca.analyze(numeric);

        final Clusters clusters = clusterer.cluster(numeric);
        final Clusters pcClusters = clusterer.cluster(principalComponents.getSubSpace());

        System.out.println(Arrays.toString(clusters.getCompactness()));
        System.out.println(clusters.getClusterCenters());
        System.out.println("-------");
        System.out.println(Arrays.toString(pcClusters.getCompactness()));
        System.out.println(pcClusters.getClusterCenters());

        ArrayList<String> newFields = new ArrayList<>(patients.fields());
        newFields.add("cluster");
        newFields.add("pcCluster");
        for (int i = 0; i < COMPONENTS; i++) {
            newFields.add("PC_" + i);
        }

        SheetWriter writer = new SheetWriter("dcis/clustered.txt", newFields);
        AtomicInteger counter = new AtomicInteger(0);
        patients.stream()
                .map(entry -> {
                    SheetEntry newCols = new SheetEntry();
                    final int count = counter.getAndIncrement();
                    newCols.put("cluster", clusters.getClusterForSample(count));
                    newCols.put("pcCluster", pcClusters.getClusterForSample(count));
                    for (int i = 0; i < COMPONENTS; i++) {
                        newCols.put("PC_" + i, principalComponents.getSubSpace().get(count, i));
                    }
                    return SheetEntry.merge(entry, newCols);
                })
                .forEach(writer);
    }
}
