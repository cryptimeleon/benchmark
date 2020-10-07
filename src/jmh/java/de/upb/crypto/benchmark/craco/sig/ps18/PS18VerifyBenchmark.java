package de.upb.crypto.benchmark.craco.sig.ps18;

import de.upb.crypto.craco.common.MessageBlock;
import de.upb.crypto.craco.common.RingElementPlainText;
import de.upb.crypto.craco.common.interfaces.PlainText;
import de.upb.crypto.craco.sig.interfaces.Signature;
import de.upb.crypto.craco.sig.interfaces.SignatureKeyPair;
import de.upb.crypto.craco.sig.interfaces.VerificationKey;
import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps18.PS18SignatureScheme;
import de.upb.crypto.craco.sig.ps18.PS18SigningKey;
import de.upb.crypto.craco.sig.ps18.PS18VerificationKey;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupProvider;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class PS18VerifyBenchmark {

    @Param({"1", "10"})
    int numMessages;

    PS18SignatureScheme scheme;
    PlainText plainText;
    Signature signature;
    VerificationKey verificationKey;

    @Setup(Level.Iteration)
    public void setup() {
        PSPublicParameters pp = new PSPublicParameters(new MclBilinearGroupProvider().provideBilinearGroup());
        scheme = new PS18SignatureScheme(pp);
        SignatureKeyPair<? extends PS18VerificationKey, ? extends PS18SigningKey> keyPair =
                scheme.generateKeyPair(numMessages);
        RingElementPlainText[] messages = new RingElementPlainText[numMessages];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = new RingElementPlainText(pp.getZp().getUniformlyRandomElement());
        }
        plainText = new MessageBlock(messages);
        signature = scheme.sign(plainText, keyPair.getSigningKey());
        verificationKey = keyPair.getVerificationKey();
    }

    @Benchmark
    @Fork(value = 1, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=psVerifyProfile.svg,simple,width=8000")
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 3, batchSize = 1)
    @Measurement(iterations = 10, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Boolean measureVerify() {
        return scheme.verify(plainText, signature, verificationKey);
    }
}