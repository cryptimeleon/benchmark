package org.cryptimeleon.benchmark.craco.sig.ps18;

import org.cryptimeleon.craco.common.plaintexts.MessageBlock;
import org.cryptimeleon.craco.common.plaintexts.PlainText;
import org.cryptimeleon.craco.common.plaintexts.RingElementPlainText;
import org.cryptimeleon.craco.sig.Signature;
import org.cryptimeleon.craco.sig.SignatureKeyPair;
import org.cryptimeleon.craco.sig.VerificationKey;
import org.cryptimeleon.craco.sig.ps.PSPublicParameters;
import org.cryptimeleon.craco.sig.ps18.PS18SignatureScheme;
import org.cryptimeleon.craco.sig.ps18.PS18SigningKey;
import org.cryptimeleon.craco.sig.ps18.PS18VerificationKey;
import org.cryptimeleon.math.structures.groups.elliptic.type3.mcl.MclBilinearGroup;
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
        PSPublicParameters pp = new PSPublicParameters(new MclBilinearGroup());
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