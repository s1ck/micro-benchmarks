package dev.s1ck;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * https://richardstartin.github.io/posts/5-java-mundane-performance-tricks#dont-iterate-over-enumvalues
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class EnumIterationBenchmark {

    @Benchmark
    public void valuesFour(Blackhole bh) {
        for (Four it : Four.values()) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void valuesEight(Blackhole bh) {
        for (Eight it : Eight.values()) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void valuesSixteen(Blackhole bh) {
        for (Sixteen it : Sixteen.values()) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void cachedFour(Blackhole bh) {
        for (Four it : Four.VALUES) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void cachedEight(Blackhole bh) {
        for (Eight it : Eight.VALUES) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void cachedSixteen(Blackhole bh) {
        for (Sixteen it : Sixteen.VALUES) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void enumSetFour(Blackhole bh) {
        for (Four it : EnumSet.allOf(Four.class)) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void enumSetEight(Blackhole bh) {
        for (Eight it : EnumSet.allOf(Eight.class)) {
            bh.consume(it.ordinal());
        }
    }

    @Benchmark
    public void enumSetSixteen(Blackhole bh) {
        for (Sixteen it : EnumSet.allOf(Sixteen.class)) {
            bh.consume(it.ordinal());
        }
    }

    enum Four {
        ONE, TWO, THREE, FOUR;

        static final Four[] VALUES = Four.values();
    }

    enum Eight {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT;

        static final Eight[] VALUES = Eight.values();
    }

    enum Sixteen {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE, THIRTEEN, FOURTEEN, FIFTEEN, SIXTEEN;

        static final Sixteen[] VALUES = Sixteen.values();
    }
}
