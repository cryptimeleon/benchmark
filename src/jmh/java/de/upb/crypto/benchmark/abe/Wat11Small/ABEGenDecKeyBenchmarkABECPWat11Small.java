package de.upb.crypto.benchmark.abe.Wat11Small;

import de.upb.crypto.benchmark.util.AttributeUtils;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11Small;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallSetup;
import de.upb.crypto.craco.interfaces.DecryptionKey;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.pe.MasterSecret;
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
public class ABEGenDecKeyBenchmarkABECPWat11Small {

    @Param({"60"})
    int securityParameter;

    @Param({"16", "24"})
    int attrSize;

    ABECPWat11Small scheme;
    MasterSecret msk;
    SetOfAttributes attributes;

    @Setup
    public void setup() {
        // setup is always run before each iteration sequence, so before first warmup iteration
        attributes = AttributeUtils.genAttributes(attrSize);
        ABECPWat11SmallSetup setup = new ABECPWat11SmallSetup();
        setup.doKeyGen(securityParameter, attributes, false);
        scheme = new ABECPWat11Small(setup.getPublicParameters());
        msk = setup.getMasterSecret();
    }

    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public DecryptionKey measureKeyGen() {
        // return key to make sure method call is not optimized away
        return scheme.generateDecryptionKey(msk, attributes);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ABEGenDecKeyBenchmarkABECPWat11Small.class.getName() + ".measureKeyGen")
                .forks(1)
                .warmupIterations(5)
                .timeUnit(TimeUnit.MILLISECONDS)
                .measurementIterations(5)
                .mode(Mode.SingleShotTime)
                .build();

        List<RunResult> results = new ArrayList<>(new Runner(opt).run());
    }
}
