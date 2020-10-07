package de.upb.crypto.benchmark.craco.sig.ps18;

import de.upb.crypto.craco.common.MessageBlock;
import de.upb.crypto.craco.common.RingElementPlainText;
import de.upb.crypto.craco.common.interfaces.PlainText;
import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps.PSPublicParametersGen;
import de.upb.crypto.craco.sig.ps18.PS18SignatureScheme;
import de.upb.crypto.craco.sig.ps18.PS18SigningKey;
import de.upb.crypto.math.serialization.Representation;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class PS18SignBenchmark {

    @Param({"1", "10"})
    int numMessages;

    @Param({"60"})
    int securityParameter;

    PS18SignatureScheme scheme;
    PS18SigningKey signKey;
    PlainText plainText;

    @Setup
    public void setup() {
        PSPublicParametersGen ppGen = new PSPublicParametersGen();
        PSPublicParameters pp = ppGen.generatePublicParameter(securityParameter, false);
        scheme = new PS18SignatureScheme(pp);
        signKey = scheme.generateKeyPair(numMessages).getSigningKey();
        RingElementPlainText[] messages = new RingElementPlainText[numMessages];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = new RingElementPlainText(pp.getZp().getUniformlyRandomElement());
        }
        plainText = new MessageBlock(messages);
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=psSignProfile.svg,simple,width=4000")
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 5, batchSize = 50)
    @Measurement(iterations = 5, batchSize = 50)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Representation measureSign() {
        return scheme.sign(plainText, signKey).getRepresentation();
    }
}