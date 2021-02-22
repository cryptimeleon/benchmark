package org.cryptimeleon.benchmark.math.groups;

import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupImpl;
import de.upb.crypto.math.pairings.generic.BilinearGroupImpl;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupImpl;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class InvComparison {

    GroupElementImpl elem1;
    GroupElementImpl elem1_1;
    GroupElementImpl elem2;
    GroupElementImpl elem2_1;
    GroupElementImpl elem3;
    GroupElementImpl elem3_1;

    @Setup
    public void setup() {
        BilinearGroupImpl bilGroupImpl = new MclBilinearGroupImpl();
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
    @Fork(value = 5)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup1Op() {
        return elem1.op(elem1_1);
    }

    @Benchmark
    @Fork(value = 5)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup1Inv() {
        return elem1.inv();
    }

    /*@Benchmark
    @Fork(value = 5)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup2Op() {
        return elem2.op(elem2_1);
    }

    @Benchmark
    @Fork(value = 5)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup2Inv() {
        return elem2.inv();
    }

    @Benchmark
    @Fork(value = 5)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup3Op() {
        return elem3.op(elem3_1);
    }

    @Benchmark
    @Fork(value = 5)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup3Inv() {
        return elem3.inv();
    }*/
}
