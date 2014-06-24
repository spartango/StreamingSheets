package com.irislabs.analysis.cluster;

/**
 * Author: spartango
 * Date: 3/3/14
 * Time: 11:31 AM.
 */
public class Cluster {
    public final int      label;
    public final double[] center;
    public final double   compactness;

    private Cluster parent;

    public Cluster(int label, double[] center, double compactness) {
        this(label, center, compactness, null);
    }

    public Cluster(int label, double[] center, double compactness, Cluster parent) {
        this.label = label;
        this.center = center;
        this.compactness = compactness;
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Cluster getParent() {
        return parent;
    }

    public void setParent(Cluster parent) {
        this.parent = parent;
    }
}
