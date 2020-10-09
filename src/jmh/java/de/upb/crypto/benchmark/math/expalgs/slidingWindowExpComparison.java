package de.upb.crypto.benchmark.math.expalgs;


/*import de.upb.crypto.math.factory.BilinearGroup;
import de.upb.crypto.math.factory.BilinearGroupFactory;
import de.upb.crypto.math.interfaces.structures.Group;
import de.upb.crypto.math.interfaces.structures.group.impl.GroupElementImpl;
import de.upb.crypto.math.structures.groups.exp.ExponentiationAlgorithms;
import de.upb.crypto.math.structures.groups.lazy.LazyGroupElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class slidingWindowExpComparison {

    @Param({"G1", "GT"})
    String groupSelection;

    GroupElementImpl elem;
    BigInteger exponent;
    Random rand = new Random(23634682);

    @Setup(Level.Iteration)
    public void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BilinearGroupFactory fac = new BilinearGroupFactory(128);
        fac.setRequirements(BilinearGroup.Type.TYPE_3);
        BilinearGroup bilGroup = fac.createBilinearGroup();
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
