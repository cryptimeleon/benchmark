package de.upb.crypto.benchmark.abe;

import de.upb.crypto.craco.abe.cp.large.ABECPWat11Setup;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallSetup;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class ABESetupBenchmarkABECPWat11 {

    @Param({"40", "60"})
    int securityParameter;

    @Param({"20", "40"})
    int lMax;

    @Param({"20", "40"})
    int n;

    @Param({"true", "false"})
    boolean watersHash;

    @Benchmark
    public void measureSetup() {
        ABECPWat11Setup setup = new ABECPWat11Setup();
        setup.doKeyGen(securityParameter, n, lMax, watersHash, false);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ABESetupBenchmarkABECPWat11.class.getName() + ".measureSetup")
                .forks(1)
                .warmupIterations(3)
                .timeUnit(TimeUnit.MILLISECONDS)
                .measurementIterations(5)
                .mode(Mode.SingleShotTime)
                .build();

        List<RunResult> results = new ArrayList<>(new Runner(opt).run());
    }
}
