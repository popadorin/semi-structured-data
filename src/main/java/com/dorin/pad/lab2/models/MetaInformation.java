package com.dorin.pad.lab2.models;

public class MetaInformation {
    private Integer numberOfConnections;

    public MetaInformation(Integer numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

    public Integer getNumberOfConnections() {
        return numberOfConnections;
    }

    @Override
    public String toString() {
        return "MetaInformation{" +
                "numberOfConnections=" + numberOfConnections +
                "}\n";
    }
}
