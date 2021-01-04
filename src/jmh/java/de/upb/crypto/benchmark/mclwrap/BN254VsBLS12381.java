package de.upb.crypto.benchmark.mclwrap;

/*import com.herumi.mcl.MclConstants;
import de.upb.crypto.math.factory.BilinearGroupImpl;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupImpl;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupImpl;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class BN254VsBLS12381 {

    @Param({"bn254", "bls12_381"})
    String curve;

    GroupElementImpl elem1;
    GroupElementImpl elem1_1;
    GroupElementImpl elem2;
    GroupElementImpl elem2_1;
    GroupElementImpl elem3;
    GroupElementImpl elem3_1;
    BilinearGroupImpl bilGroupImpl;;

    @Setup(Level.Iteration)
    public void setup() {
        if (curve.equals("bn254")) {
            bilGroupImpl = new MclBilinearGroupImpl(MclConstants.BN254);
        } else {
            bilGroupImpl = new MclBilinearGroupImpl(MclConstants.BLS12_381);
        }
        GroupImpl group1 = bilGroupImpl.getG1();
        GroupImpl group2 = bilGroupImpl.getG2();
        GroupImpl group3 = bilGroupImpl.getGT();
        elem1 = group1.getUniformlyRandomNonNeutral();
        elem1_1 = group1.getUniformlyRandomNonNeutral();
        elem2 = group2.getUniformlyRandomNonNeutral();
        elem2_1 = group2.getUniformlyRandomNonNeutral();
        elem3 = group3.getUniformlyRandomNonNeutral();
        elem3_1 = group3.getUniformlyRandomNonNeutral();
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 4, time = 1)
    @Measurement(iterations = 8, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup1Op() {
        return elem1.op(elem1_1);
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 4, time = 1)
    @Measurement(iterations = 8, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup2Op() {
        return elem2.op(elem2_1);
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 4, time = 1)
    @Measurement(iterations = 8, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup3Op() {
        return elem3.op(elem3_1);
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 4, time = 1)
    @Measurement(iterations = 8, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measurePairing() {
        return bilGroupImpl.getBilinearMap().apply(elem1, elem2);
    }
}
*/