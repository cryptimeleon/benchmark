package de.upb.crypto.craco.sig.generic;

public class SigBenchmarkConfigBuilder {
    private int numWarmupRuns;
    private int numSetups;
    private int numKeyGenerations;
    private int signVfyCycles;

    private boolean printDetails;

    public SigBenchmarkConfigBuilder() {
        numWarmupRuns = 1;
        numSetups = 1;
        numKeyGenerations = 1;
        signVfyCycles = 1;
        printDetails = true;
    }

    public SigBenchmarkConfig buildConfig() {
        return new SigBenchmarkConfig(numSetups, numKeyGenerations, signVfyCycles, numWarmupRuns, printDetails);
    }

    public SigBenchmarkConfigBuilder setNumWarmupRuns(int numWarmupRuns) {
        this.numWarmupRuns = numWarmupRuns;
        return this;
    }

    public SigBenchmarkConfigBuilder setNumSetups(int numSetups) {
        this.numSetups = numSetups;
        return this;
    }

    public SigBenchmarkConfigBuilder setNumKeyGenerations(int numKeyGenerations) {
        this.numKeyGenerations = numKeyGenerations;
        return this;
    }

    public SigBenchmarkConfigBuilder setSignVfyCycles(int signVfyCycles) {
        this.signVfyCycles = signVfyCycles;
        return this;
    }

    public SigBenchmarkConfigBuilder setPrintDetails(boolean printDetails) {
        this.printDetails = printDetails;
        return this;
    }
}
