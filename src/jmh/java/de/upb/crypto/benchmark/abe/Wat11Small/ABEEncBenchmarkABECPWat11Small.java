package de.upb.crypto.benchmark.abe.Wat11Small;

import de.upb.crypto.benchmark.util.AttributeUtils;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11Small;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallSetup;
import de.upb.crypto.craco.common.GroupElementPlainText;
import de.upb.crypto.craco.interfaces.CipherText;
import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.policy.BooleanPolicy;
import de.upb.crypto.math.serialization.Representation;
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
public class ABEEncBenchmarkABECPWat11Small {

    @Param({"60"})
    int securityParameter;

    @Param({"16", "24"})
    int attrSize;

    EncryptionKey encKey;
    PlainText plainText;
    ABECPWat11Small scheme;

    @Setup
    public void setup() {
        // setup is always run before each iteration sequence, so before first warmup iteration
        ABECPWat11SmallSetup setup = new ABECPWat11SmallSetup();
        setup.doKeyGen(securityParameter, AttributeUtils.genAttributes(attrSize), false);
        scheme = new ABECPWat11Small(setup.getPublicParameters());
        encKey = scheme.generateEncryptionKey(
                new BooleanPolicy(BooleanPolicy.BooleanOperator.AND, AttributeUtils.genAttributes(attrSize))
        );
        plainText = new GroupElementPlainText(
                setup.getPublicParameters().getGroupGT().getUniformlyRandomNonNeutral()
        );
    }


    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public Representation measureEncrypt() {
        return scheme.encrypt(plainText, encKey).getRepresentation();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ABEEncBenchmarkABECPWat11Small.class.getName() + ".measureEncrypt")
                .forks(1)
                .measurementBatchSize(1)
                .warmupIterations(5)
                .timeUnit(TimeUnit.MILLISECONDS)
                .measurementIterations(5)
                .mode(Mode.SingleShotTime)
                .build();

        List<RunResult> results = new ArrayList<>(new Runner(opt).run());
    }
}
