package de.upb.crypto.craco.sig;

import de.upb.crypto.craco.sig.generic.SigBenchmark;
import de.upb.crypto.craco.sig.generic.SigBenchmarkConfig;
import de.upb.crypto.craco.sig.generic.SigBenchmarkConfigBuilder;
import de.upb.crypto.craco.sig.generic.SigBenchmarkParams;

public class PSBenchmark {

    public static void main(String[] args) {
        SigBenchmarkParams params = new PSBenchmarkParams();
        SigBenchmarkConfig config = new SigBenchmarkConfigBuilder().setNumWarmupRuns(2)
                .setNumSetups(5).buildConfig();
        SigBenchmark benchmark = new SigBenchmark(params, config);
        benchmark.doBenchmark();
    }
}
