package de.upb.crypto.benchmark.abe;

import de.upb.crypto.benchmark.util.AttributeUtils;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11Small;
import de.upb.crypto.craco.abe.cp.small.ABECPWat11SmallSetup;
import de.upb.crypto.craco.common.GroupElementPlainText;
import de.upb.crypto.craco.interfaces.CipherText;
import de.upb.crypto.craco.interfaces.DecryptionKey;
import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.abe.SetOfAttributes;
import de.upb.crypto.craco.interfaces.policy.BooleanPolicy;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class ABEDecBenchmarkABECPWat11Small {

    @Param({"60"})
    int securityParameter;

    @Param({"8", "16", "24", "32"})
    int attrSize;

    DecryptionKey decKey;
    CipherText cipherText;
    ABECPWat11Small scheme;

    @Setup
    public void setup() {
        // setup is always run before each iteration sequence, so before first warmup iteration
        ABECPWat11SmallSetup setup = new ABECPWat11SmallSetup();
        setup.doKeyGen(securityParameter, AttributeUtils.genAttributes(attrSize), false);
        scheme = new ABECPWat11Small(setup.getPublicParameters());
        SetOfAttributes attributes = AttributeUtils.genAttributes(attrSize);
        EncryptionKey encKey = scheme.generateEncryptionKey(
                new BooleanPolicy(BooleanPolicy.BooleanOperator.AND, attributes)
        );
        PlainText plainText = new GroupElementPlainText(
                setup.getPublicParameters().getGroupGT().getUniformlyRandomNonNeutral()
        );
        cipherText = scheme.encrypt(plainText, encKey);
        decKey = scheme.generateDecryptionKey(setup.getMasterSecret(), attributes);
    }


    @Benchmark
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public PlainText measureDecrypt() {
        return scheme.decrypt(cipherText, decKey);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ABEDecBenchmarkABECPWat11Small.class.getName() + ".measureDecrypt")
                .forks(1)
                .measurementBatchSize(1)
                .warmupIterations(5)
                .timeUnit(TimeUnit.MILLISECONDS)
                .measurementIterations(5)
                .mode(Mode.SingleShotTime)
                .output("benchresults")
                .resultFormat(ResultFormatType.LATEX)
                .build();

        List<RunResult> results = new ArrayList<>(new Runner(opt).run());
    }
}
