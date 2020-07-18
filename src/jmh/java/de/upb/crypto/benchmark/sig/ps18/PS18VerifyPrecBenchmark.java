package de.upb.crypto.benchmark.sig.ps18;

import de.upb.crypto.craco.common.MessageBlock;
import de.upb.crypto.craco.common.RingElementPlainText;
import de.upb.crypto.craco.interfaces.PlainText;
import de.upb.crypto.craco.interfaces.signature.Signature;
import de.upb.crypto.craco.interfaces.signature.SignatureKeyPair;
import de.upb.crypto.craco.interfaces.signature.VerificationKey;
import de.upb.crypto.craco.sig.ps.PSPublicParameters;
import de.upb.crypto.craco.sig.ps.PSPublicParametersGen;
import de.upb.crypto.craco.sig.ps18.PS18SignatureSchemePrec;
import de.upb.crypto.craco.sig.ps18.PS18SigningKey;
import de.upb.crypto.craco.sig.ps18.PS18VerificationKey;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroup;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupProvider;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class PS18VerifyPrecBenchmark {

    @Param({"1", "10", "100"})
    int numMessages;

    PS18SignatureSchemePrec scheme;
    PlainText plainText;
    Signature signature;
    VerificationKey verificationKey;

    @Setup(Level.Iteration)
    public void setup() {
        PSPublicParameters pp = new PSPublicParameters(new MclBilinearGroup().getBilinearMap());
        scheme = new PS18SignatureSchemePrec(pp);
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
    //@Fork(value = 1, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
    //        ",file=psPrecVerifyProfile")
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 3, batchSize = 1)
    @Measurement(iterations = 10, batchSize = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void measureVerify() {
        scheme.verify(plainText, signature, verificationKey);
    }
}