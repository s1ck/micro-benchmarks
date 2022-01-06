package dev.s1ck;

import org.apache.commons.math3.util.Pair;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * https://richardstartin.github.io/posts/5-java-mundane-performance-tricks#use-wrappers-for-composite-hashmap-keys
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CompositeLookupBenchmark {
    @Param("1024")
    int size;

    Map<String, Object> concatMap;
    Map<Pair, Object> pairMap;
    String[] prefixes;
    String[] suffixes;

    @Setup(Level.Trial)
    public void setup() {
        prefixes = new String[size];
        suffixes = new String[size];
        concatMap = new HashMap<>();
        pairMap = new HashMap<>();
        for (int i = 0; i < size; ++i) {
            prefixes[i] = UUID.randomUUID().toString();
            suffixes[i] = UUID.randomUUID().toString();
            concatMap.put(prefixes[i] + ";" + suffixes[i], i);
            // use new String to avoid reference equality speeding up the equals calls
            //noinspection StringOperationCanBeSimplified
            pairMap.put(new Pair(new String(prefixes[i]), new String(suffixes[i])), i);
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void concatenate(Blackhole bh) {
        for (int i = 0; i < prefixes.length; ++i) {
            bh.consume(concatMap.get(prefixes[i] + ";" + suffixes[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void wrap(Blackhole bh) {
        for (int i = 0; i < prefixes.length; ++i) {
            bh.consume(pairMap.get(new Pair(prefixes[i], suffixes[i])));
        }
    }
}
