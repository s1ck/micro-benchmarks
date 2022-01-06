package dev.s1ck;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;

/**
 * https://richardstartin.github.io/posts/5-java-mundane-performance-tricks#size-hashmaps-whenever-possible
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
public class HashMapResizeBenchmark {

    @Param({"10", "14"})
    int keys;

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
