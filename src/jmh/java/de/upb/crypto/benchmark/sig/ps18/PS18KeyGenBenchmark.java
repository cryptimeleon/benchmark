package de.upb.crypto.benchmark.sig.ps18;

import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps.PSPublicParametersGen;
import de.upb.crypto.craco.sig.ps18.PS18SignatureScheme;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class PS18KeyGenBenchmark {

    @Param({"1", "10", "100"})
    int numMessages;

    @Param({"60", "120"})
    int securityParameter;

    PS18SignatureScheme scheme;

    @Setup
    public void setup() {
        PSPublicParametersGen ppGen = new PSPublicParametersGen();
        PSPublicParameters pp = ppGen.generatePublicParameter(securityParameter, false);
        scheme = new PS18SignatureScheme(pp);
    }

    @Benchmark
    @Fork(1)
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureKeyGen() {
        scheme.generateKeyPair(numMessages);
    }
}
