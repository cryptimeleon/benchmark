package de.upb.crypto.benchmark.math.groups;

import de.upb.crypto.math.factory.BilinearGroup;
import de.upb.crypto.math.factory.BilinearGroupFactory;
import de.upb.crypto.math.interfaces.structures.Group;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.pairings.mcl.MclBilinearGroupProvider;
import de.upb.crypto.math.structures.groups.lazy.LazyGroupElement;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class InvComparison {

    GroupElementImpl elem1;
    GroupElementImpl elem1_1;
    GroupElementImpl elem2;
    GroupElementImpl elem2_1;

    @Setup(Level.Iteration)
    public void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        /*BilinearGroupFactory fac = new BilinearGroupFactory(128);
        fac.setRequirements(BilinearGroup.Type.TYPE_1);
        BilinearGroup bilGroup = fac.createBilinearGroup();*/
        BilinearGroup bilGroup = new MclBilinearGroupProvider().provideBilinearGroup();
        Group group1 = bilGroup.getG1();
        Group group2 = bilGroup.getGT();
        Method getValue = LazyGroupElement.class.getDeclaredMethod("getConcreteValue");
        getValue.setAccessible(true);
        elem1 = (GroupElementImpl) getValue.invoke(group1.getUniformlyRandomNonNeutral());
        elem1_1 = (GroupElementImpl) getValue.invoke(group1.getUniformlyRandomNonNeutral());
        elem2 = (GroupElementImpl) getValue.invoke(group2.getUniformlyRandomNonNeutral());
        elem2_1 = (GroupElementImpl) getValue.invoke(group2.getUniformlyRandomNonNeutral());
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup1Op() {
        return elem1.op(elem1_1);
    }

    @Benchmark
    @Fork(value = 3, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=results/g1Inv.svg,simple,width=6000")
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup1Inv() {
        return elem1.inv();
    }

    @Benchmark
    @Fork(value = 3)
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 3, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup2Op() {
        return elem2.op(elem2_1);
    }

    @Benchmark
    @Fork(value = 3, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=results/gTInv.svg,simple,width=6000")
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureGroup2Inv() {
        return elem2.inv();
    }
}
