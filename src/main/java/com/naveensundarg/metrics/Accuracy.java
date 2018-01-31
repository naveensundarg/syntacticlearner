package com.naveensundarg.metrics;

public class Accuracy implements Metrics{

    private final double accuracy;

    public Accuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }
}
