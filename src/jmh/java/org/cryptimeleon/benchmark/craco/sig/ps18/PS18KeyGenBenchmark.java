package org.cryptimeleon.benchmark.craco.sig.ps18;

import org.cryptimeleon.craco.sig.SignatureKeyPair;
import org.cryptimeleon.craco.sig.SigningKey;
import org.cryptimeleon.craco.sig.VerificationKey;
import org.cryptimeleon.craco.sig.ps.PSPublicParameters;
import org.cryptimeleon.craco.sig.ps.PSPublicParametersGen;
import org.cryptimeleon.craco.sig.ps18.PS18SignatureScheme;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class PS18KeyGenBenchmark {

    @Param({"1", "10"})
    int numMessages;

    @Param({"60"})
    int securityParameter;

    PS18SignatureScheme scheme;

    @Setup
    public void setup() {
        PSPublicParametersGen ppGen = new PSPublicParametersGen();
        PSPublicParameters pp = ppGen.generatePublicParameter(securityParameter, false);
        scheme = new PS18SignatureScheme(pp);
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=psKeyGenProfile.svg,simple,width=4000")
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureKeyGen(Blackhole bh) {
        SignatureKeyPair<? extends VerificationKey, ? extends SigningKey> keypair = scheme.generateKeyPair(numMessages);
        bh.consume(keypair.getSigningKey().getRepresentation());
        bh.consume(keypair.getVerificationKey().getRepresentation());
    }
}
