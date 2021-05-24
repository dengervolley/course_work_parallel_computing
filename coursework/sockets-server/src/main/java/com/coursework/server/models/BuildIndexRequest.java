package com.coursework.server.models;

public class BuildIndexRequest {
    private int numFiles;
    private int numThreads;

    public BuildIndexRequest() {
    }

    public BuildIndexRequest(int numFiles, int numThreads) {
        this.numFiles = numFiles;
        this.numThreads = numThreads;
    }

    public int getNumFiles() {
        return this.numFiles;
    }

    public int getNumThreads() {
        return this.numThreads;
    }
}
