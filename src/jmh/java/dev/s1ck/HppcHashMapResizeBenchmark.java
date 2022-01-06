package dev.s1ck;

import com.carrotsearch.hppc.IntIntHashMap;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;

@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
public class HppcHashMapResizeBenchmark {

    @Param({"10", "24"})
    int keys;

    // cap 10 (16)
    // keys 10 => resizeAt = 12 -> no resize
    // keys 26 => resizeAt = 12 -> resize -> resizeAt = 24
    //
    // cap 24 (32)
    // keys 10 => resizeAt = 24 -> no resize
    // keys 26 => resizeAt = 24 -> no resize
    @Param({"10", "24"})
    int capacity;

    @Benchmark
    public IntIntHashMap loadHppcHashMap() {
        IntIntHashMap map = new IntIntHashMap(capacity);
        for (int i = 0; i < keys; ++i) {
            map.put(i, i);
        }
        return map;
    }

}
