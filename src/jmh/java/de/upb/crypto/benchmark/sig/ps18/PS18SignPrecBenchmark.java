package de.upb.crypto.benchmark.sig.ps18;

import de.upb.crypto.craco.common.MessageBlock;
import de.upb.crypto.craco.common.RingElementPlainText;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps.PSPublicParametersGen;
import de.upb.crypto.craco.sig.ps18.PS18SignatureSchemePrec;
import de.upb.crypto.craco.sig.ps18.PS18SigningKey;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class PS18SignPrecBenchmark {

    @Param({"1", "10", "100"})
    int numMessages;

    @Param({"60", "80"})
    int securityParameter;

    PS18SignatureSchemePrec scheme;
    PS18SigningKey signKey;
    PlainText plainText;

    @Setup
    public void setup() {
        PSPublicParametersGen ppGen = new PSPublicParametersGen();
        PSPublicParameters pp = ppGen.generatePublicParameter(securityParameter, false);
        scheme = new PS18SignatureSchemePrec(pp);
        signKey = scheme.generateKeyPair(numMessages).getSigningKey();
        RingElementPlainText[] messages = new RingElementPlainText[numMessages];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = new RingElementPlainText(pp.getZp().getUniformlyRandomElement());
        }
        plainText = new MessageBlock(messages);
    }

    @Benchmark
    @Fork(1)
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 5, batchSize = 50)
    @Measurement(iterations = 5, batchSize = 50)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureSign() {
        scheme.sign(plainText, signKey);
    }
}