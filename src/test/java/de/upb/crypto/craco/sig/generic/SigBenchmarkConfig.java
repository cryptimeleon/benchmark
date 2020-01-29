package de.upb.crypto.craco.sig.generic;

public class SigBenchmarkConfig {

    private int numWarmupRuns;
    private int numSetups;
    private int numKeyGenerations;
    private int signVfyCycles;

    private boolean printDetails;


    public SigBenchmarkConfig(int numSetups, int numKeyGenerations, int signVfyCycles, int numWarmupRuns,
                              boolean printDetails) {
        this.numSetups = numSetups;
        this.numKeyGenerations = numKeyGenerations;
        this.signVfyCycles = signVfyCycles;
        this.numWarmupRuns = numWarmupRuns;
        this.printDetails = printDetails;
    }

    public int getNumWarmupRuns() {
        return numWarmupRuns;
    }

    public int getNumSetups() {
        return numSetups;
    }

    public int getNumKeyGenerations() {
        return numKeyGenerations;
    }

    public int getSignVfyCycles() {
        return signVfyCycles;
    }

    public boolean isPrintDetails() {
        return printDetails;
    }
}
