package de.upb.crypto.benchmark.abe;

import de.upb.crypto.benchmark.util.AttributeUtils;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallSetup;
import de.upb.crypto.craco.interfaces.abe.BigIntegerAttribute;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import org.junit.Test;
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
public class ABESetupBenchmarkABECPWat11Small {

    @Param({"40", "60"})
    int securityParameter;

    @Param({"16", "24"})
    int attrSize;

    SetOfAttributes attributes;

    @Setup
    public void setup() {
        attributes = AttributeUtils.genAttributes(attrSize);
    }

    @Benchmark
    public void measureSetup() {
        ABECPWat11SmallSetup setup = new ABECPWat11SmallSetup();
        setup.doKeyGen(securityParameter, attributes, false);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ABESetupBenchmarkABECPWat11Small.class.getName() + ".measureSetup")
                .forks(1)
                .warmupIterations(3)
                .timeUnit(TimeUnit.MILLISECONDS)
                .measurementIterations(5)
                .mode(Mode.SingleShotTime)
                .build();

        List<RunResult> results = new ArrayList<>(new Runner(opt).run());
    }
}
