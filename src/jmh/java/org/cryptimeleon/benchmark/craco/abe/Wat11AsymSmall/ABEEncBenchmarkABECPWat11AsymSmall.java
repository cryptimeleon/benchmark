package org.cryptimeleon.benchmark.craco.abe.Wat11AsymSmall;

import org.cryptimeleon.benchmark.util.AttributeUtils;
import de.upb.crypto.craco.abe.cp.small.asymmetric.ABECPWat11AsymSmall;
import de.upb.crypto.craco.abe.cp.small.asymmetric.ABECPWat11AsymSmallSetup;
import de.upb.crypto.craco.common.GroupElementPlainText;
import de.upb.crypto.craco.common.interfaces.EncryptionKey;
import de.upb.crypto.craco.common.interfaces.PlainText;
import de.upb.crypto.craco.common.interfaces.policy.BooleanPolicy;
import de.upb.crypto.math.serialization.Representation;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class ABEEncBenchmarkABECPWat11AsymSmall {

    @Param({"60"})
    int securityParameter;

    @Param({"16", "24"})
    int attrSize;

    EncryptionKey encKey;
    PlainText plainText;
    ABECPWat11AsymSmall scheme;

    @Setup
    public void setup() {
        // setup is always run before each iteration sequence, so before first warmup iteration
        ABECPWat11AsymSmallSetup setup = new ABECPWat11AsymSmallSetup();
        setup.doKeyGen(securityParameter, AttributeUtils.genAttributes(attrSize), false);
        scheme = new ABECPWat11AsymSmall(setup.getPublicParameters());
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
}
