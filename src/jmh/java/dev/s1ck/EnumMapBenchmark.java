package dev.s1ck;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.SplittableRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * https://richardstartin.github.io/posts/5-java-mundane-performance-tricks#use-enums-instead-of-constant-strings
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class EnumMapBenchmark {

    @Benchmark
    public void longEnumMap(LongEnumMapState state, Blackhole bh) {
        for (ALongEnum value : state.values) {
            bh.consume(state.map.get(value));
        }
    }

    @Benchmark
    public void longHashMap(LongHashMapState state, Blackhole bh) {
        for (String value : state.values) {
            bh.consume(state.map.get(value));
        }
    }

    @Benchmark
    public void shortEnumMap(ShortEnumMapState state, Blackhole bh) {
        for (AShortEnum value : state.values) {
            bh.consume(state.map.get(value));
        }
    }

    @Benchmark
    public void shortHashMap(ShortHashMapState state, Blackhole bh) {
        for (String value : state.values) {
            bh.consume(state.map.get(value));
        }
    }

    @State(Scope.Benchmark)
    public static class BaseState {
        @Param("10000")
        int size;

        @Param("42")
        int seed;

        int[] randomValues;

        @Setup(Level.Trial)
        public void setup() {
            SplittableRandom random = new SplittableRandom(seed);
            randomValues = new int[size];
            for (int i = 0; i < size; i++) {
                randomValues[i] = random.nextInt(0, Integer.MAX_VALUE);
            }
            fill(randomValues);
        }

        void fill(int[] randomValues) {}
    }

    public static class LongEnumMapState extends BaseState {

        EnumMap<ALongEnum, String> map;
        ALongEnum[] values;

        @Override
        void fill(int[] randomValues) {
            map = new EnumMap<>(ALongEnum.class);
            values = new ALongEnum[randomValues.length];
            ALongEnum[] enumValues = ALongEnum.values();
            int pos = 0;
            for (int i : randomValues) {
                values[pos++] = enumValues[i % enumValues.length];
            }
            for (ALongEnum value : enumValues) {
                map.put(value, UUID.randomUUID().toString());
            }
        }
    }

    public static class LongHashMapState extends BaseState {

        HashMap<String, String> map;
        String[] values;

        @Override
        void fill(int[] randomValues) {
            map = new HashMap<>();
            values = new String[randomValues.length];
            ALongEnum[] enumValues = ALongEnum.values();
            int pos = 0;
            for (int i : randomValues) {
                values[pos++] = enumValues[i % enumValues.length].toString();
            }
            for (ALongEnum value : enumValues) {
                map.put(value.toString(), UUID.randomUUID().toString());
            }
        }
    }

    public static class ShortEnumMapState extends BaseState {

        EnumMap<AShortEnum, String> map;
        AShortEnum[] values;

        @Override
        void fill(int[] randomValues) {
            map = new EnumMap<>(AShortEnum.class);
            values = new AShortEnum[randomValues.length];
            AShortEnum[] enumValues = AShortEnum.values();
            int pos = 0;
            for (int i : randomValues) {
                values[pos++] = enumValues[i % enumValues.length];
            }
            for (AShortEnum value : enumValues) {
                map.put(value, UUID.randomUUID().toString());
            }
        }
    }

    public static class ShortHashMapState extends BaseState {

        HashMap<String, String> map;
        String[] values;

        @Override
        void fill(int[] randomValues) {
            map = new HashMap<>();
            values = new String[randomValues.length];
            AShortEnum[] enumValues = AShortEnum.values();
            int pos = 0;
            for (int i : randomValues) {
                values[pos++] = enumValues[i % enumValues.length].toString();
            }
            for (AShortEnum value : enumValues) {
                map.put(value.toString(), UUID.randomUUID().toString());
            }
        }
    }

    enum ALongEnum {
        ONE_VERY_LONG_STRING,
        TWO_VERY_LONG_STRING,
        THREE_VERY_LONG_STRING,
        FOUR_VERY_LONG_STRING,
        FIVE_VERY_LONG_STRING,
        SIX_VERY_LONG_STRING,
        SEVEN_VERY_LONG_STRING,
        EIGHT_VERY_LONG_STRING,
        NINE_VERY_LONG_STRING,
        TEN_VERY_LONG_STRING,
    }

    enum AShortEnum {
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
    }
}
