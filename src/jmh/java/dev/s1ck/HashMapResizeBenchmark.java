package dev.s1ck;

import com.carrotsearch.hppc.IntIntHashMap;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * https://richardstartin.github.io/posts/5-java-mundane-performance-tricks#size-hashmaps-whenever-possible
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HashMapResizeBenchmark {

    @Param({"10", "14"})
    int keys;

    // cap 16 (16)
    // keys 10 => resizeAt = 12 -> no resize
    // keys 24 => resizeAt = 12 -> resize -> resizeAt = 24
    //
    // cap 24 (32)
    // keys 10 => resizeAt = 24 -> no resize
    // keys 24 => resizeAt = 24 -> no resize
    @Param({"16", "24"})
    int capacity;

    @Benchmark
    public HashMap<Integer, Integer> loadHashMap() {
        HashMap<Integer, Integer> map = new HashMap<>(capacity);
        for (int i = 0; i < keys; ++i) {
            map.put(i, i);
        }
        return map;
    }
}
