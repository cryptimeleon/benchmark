package org.cryptimeleon.benchmark.math.expalgs;

/*import org.cryptimeleon.math.structures.groups.GroupElementImpl;
import org.cryptimeleon.math.structures.groups.Group;
import org.cryptimeleon.math.structures.groups.elliptic.BilinearGroup;
import org.cryptimeleon.math.structures.groups.elliptic.type3.bn.BarretoNaehrigBilinearGroup;
import org.cryptimeleon.math.structures.groups.exp.ExponentiationAlgorithms;
import org.cryptimeleon.math.structures.groups.lazy.LazyGroupElement;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class SlidingWindowExpComparison {

    @Param({"G1", "GT"})
    String groupSelection;

    GroupElementImpl elem;
    BigInteger exponent;
    Random rand = new Random(23634682);

    @Setup(Level.Iteration)
    public void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BilinearGroup bilGroup = new BarretoNaehrigBilinearGroup(128);
        Group selectedGroup;
        switch (groupSelection) {
            case "G1":
                selectedGroup = bilGroup.getG1();
                break;
            case "G2":
                selectedGroup = bilGroup.getG2();
                break;
            case "GT":
                selectedGroup = bilGroup.getGT();
                break;
            default:
                throw new IllegalArgumentException("Invalid selected group " + groupSelection);
        }
        Method getValue = LazyGroupElement.class.getDeclaredMethod("getConcreteValue");
        getValue.setAccessible(true);
        elem = (GroupElementImpl) getValue.invoke(selectedGroup.getUniformlyRandomNonNeutral());
        exponent = BigInteger.valueOf(rand.nextLong());
    }

    @Benchmark
    @Fork(value = 3, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=results/slidingA3.svg,simple,width=8000")
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 5)
    @Measurement(iterations = 2, time = 5)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureA1() {
        return ExponentiationAlgorithms.slidingWindowExpA1(elem, exponent, null, 4);
    }

    @Benchmark
    @Fork(value = 3, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=results/slidingA2.svg,simple,width=8000")
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 5)
    @Measurement(iterations = 2, time = 5)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureA2() {
        return ExponentiationAlgorithms.slidingWindowExpA2(elem, exponent, null, 4);
    }

    @Benchmark
    @Fork(value = 3, jvmArgsAppend = "-agentpath:/home/raphael/async-profiler/build/libasyncProfiler.so=start" +
            ",file=results/slidingA1.svg,simple,width=8000")
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1, time = 5)
    @Measurement(iterations = 2, time = 5)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public GroupElementImpl measureA3() {
        return ExponentiationAlgorithms.slidingWindowExpA3(elem, exponent, null, 4);
    }
}*/
