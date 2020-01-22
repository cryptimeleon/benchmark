package de.upb.crypto.craco.abe;

import de.upb.crypto.craco.abe.generic.ABEBenchmark;
import de.upb.crypto.craco.abe.generic.ABEBenchmarkConfig;
import de.upb.crypto.craco.abe.generic.ABEBenchmarkConfigBuilder;
import de.upb.crypto.craco.abe.generic.ABEBenchmarkParams;

public class ABEWat11SmallBenchmark {

    public static void main(String[] args) {
        ABEBenchmarkParams params = new ABEWat11SmallBenchmarkParams();
        ABEBenchmarkConfig config = new ABEBenchmarkConfigBuilder().setIsCPABE(true).buildConfig();
        ABEBenchmark benchmark = new ABEBenchmark(params, config);
        benchmark.doBenchmark();
    }
}
