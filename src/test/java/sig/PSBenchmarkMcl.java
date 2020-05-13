package sig;

import de.upb.crypto.benchmark.sig.generic.SigBenchmark;
import de.upb.crypto.benchmark.sig.generic.SigBenchmarkConfig;
import de.upb.crypto.benchmark.sig.generic.SigBenchmarkConfigBuilder;
import de.upb.crypto.benchmark.sig.generic.SigBenchmarkParams;

public class PSBenchmarkMcl {

    public static void main(String[] args) {
        SigBenchmarkParams params = new PSBenchmarkParamsMcl();
        SigBenchmarkConfig config = new SigBenchmarkConfigBuilder().setNumWarmupRuns(10)
                .setNumSetups(10).setNumKeyGenerations(10).setSignVfyCycles(10)
                .setPrintDetails(false).buildConfig();
        SigBenchmark benchmark = new SigBenchmark(params, config);
        benchmark.doBenchmark();
    }
}
