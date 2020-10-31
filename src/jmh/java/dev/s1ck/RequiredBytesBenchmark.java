package dev.s1ck;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class RequiredBytesBenchmark {

    @Param({"10000000", "100000000"})
    int size;

    private int[] input;

    private Random r = new Random(42);

    @Setup
    public void setup() {
        input = new int[size];

        for (int i = 0; i < size; i++) {
            input[i] = Math.abs(r.nextInt());
        }
    }

    @Benchmark
    public int branching() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += RequiredBytes.branching(input[i]);
        }
        return sum;
    }

    @Benchmark
    public int leadingZeros() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += RequiredBytes.leadingZeros(input[i]);
        }
        return sum;
    }
}
