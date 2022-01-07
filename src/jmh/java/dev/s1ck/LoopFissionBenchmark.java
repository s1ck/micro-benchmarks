package dev.s1ck;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * https://richardstartin.github.io/posts/loop-fission#xor
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LoopFissionBenchmark {

    @Param({"256", "512", "1024", "2048", "4096"})
    int size;

    private long[] left;
    private long[] right;

    @Setup(Level.Trial)
    public void setup() {
        left = new long[size];
        right = new long[size];
        for (int i = 0; i < size; i++) {
            left[i] = ThreadLocalRandom.current().nextLong();
            right[i] = ThreadLocalRandom.current().nextLong();
        }
    }

    @Benchmark
    public int fused() {
        int count = 0;
        //  0.31%    ↗│││   0x00007f6eac35baa0: mov    0x10(%r10,%r11,8),%rsi
        //  2.28%    ││││   0x00007f6eac35baa5: xor    0x10(%rbx,%r11,8),%rsi
        //           ││││
        //           ││││
        //  7.69%    ││││   0x00007f6eac35baaa: mov    %rsi,0x10(%rbx,%r11,8)
        //           ││││
        //           ││││
        //  2.06%    ││││   0x00007f6eac35baaf: popcnt %rsi,%rsi
        //  8.92%    ││││   0x00007f6eac35bab4: add    %esi,%edx
        for (int i = 0; i < left.length & i < right.length; i++) {
            left[i] ^= right[i];
            count += Long.bitCount(left[i]);
        }
        return count;
    }

    @Benchmark
    public int fissured() {
        //  1.11%  │    │││ │↗      ││  0x00007f1bc835b892: vmovdqu 0x10(%rdi,%rdx,8),%ymm0
        //  0.31%  │    │││ ││      ││  0x00007f1bc835b898: vpxor  0x10(%r11,%rdx,8),%ymm0,%ymm0
        //  0.40%  │    │││ ││      ││  0x00007f1bc835b89f: vmovdqu %ymm0,0x10(%r11,%rdx,8)
        //  1.09%  │    │││ ││      ││  0x00007f1bc835b8a6: vmovdqu 0x30(%rdi,%rdx,8),%ymm0
        //  0.44%  │    │││ ││      ││  0x00007f1bc835b8ac: vpxor  0x30(%r11,%rdx,8),%ymm0,%ymm0
        //  1.69%  │    │││ ││      ││  0x00007f1bc835b8b3: vmovdqu %ymm0,0x30(%r11,%rdx,8)
        //  0.42%  │    │││ ││      ││  0x00007f1bc835b8ba: vmovdqu 0x50(%rdi,%rdx,8),%ymm0
        //  0.31%  │    │││ ││      ││  0x00007f1bc835b8c0: vpxor  0x50(%r11,%rdx,8),%ymm0,%ymm0
        //  0.77%  │    │││ ││      ││  0x00007f1bc835b8c7: vmovdqu %ymm0,0x50(%r11,%rdx,8)
        //  0.54%  │    │││ ││      ││  0x00007f1bc835b8ce: vmovdqu 0x70(%rdi,%rdx,8),%ymm0
        //  0.42%  │    │││ ││      ││  0x00007f1bc835b8d4: vpxor  0x70(%r11,%rdx,8),%ymm0,%ymm0
        //  2.28%  │    │││ ││      ││  0x00007f1bc835b8db: vmovdqu %ymm0,0x70(%r11,%rdx,8)
        //  0.33%  │    │││ ││      ││  0x00007f1bc835b8e2: vmovdqu 0x90(%rdi,%rdx,8),%ymm0
        //  0.46%  │    │││ ││      ││  0x00007f1bc835b8eb: vpxor  0x90(%r11,%rdx,8),%ymm0,%ymm0
        //  1.13%  │    │││ ││      ││  0x00007f1bc835b8f5: vmovdqu %ymm0,0x90(%r11,%rdx,8)
        //  0.19%  │    │││ ││      ││  0x00007f1bc835b8ff: vmovdqu 0xb0(%rdi,%rdx,8),%ymm0
        //  0.36%  │    │││ ││      ││  0x00007f1bc835b908: vpxor  0xb0(%r11,%rdx,8),%ymm0,%ymm0
        //  2.38%  │    │││ ││      ││  0x00007f1bc835b912: vmovdqu %ymm0,0xb0(%r11,%rdx,8)
        //  0.13%  │    │││ ││      ││  0x00007f1bc835b91c: vmovdqu 0xd0(%rdi,%rdx,8),%ymm0
        //  0.40%  │    │││ ││      ││  0x00007f1bc835b925: vpxor  0xd0(%r11,%rdx,8),%ymm0,%ymm0
        //  1.44%  │    │││ ││      ││  0x00007f1bc835b92f: vmovdqu %ymm0,0xd0(%r11,%rdx,8)
        //  0.15%  │    │││ ││      ││  0x00007f1bc835b939: vmovdqu 0xf0(%rdi,%rdx,8),%ymm0
        //  0.23%  │    │││ ││      ││  0x00007f1bc835b942: vpxor  0xf0(%r11,%rdx,8),%ymm0,%ymm0
        //  2.84%  │    │││ ││      ││  0x00007f1bc835b94c: vmovdqu %ymm0,0xf0(%r11,%rdx,8)
        for (int i = 0; i < left.length & i < right.length; i++) {
            left[i] ^= right[i];
        }
        //  0.09%  │││              ↗   0x00007f3b1c35bd02: popcnt 0x28(%r11,%rbx,8),%r8
        // 12.50%  │││              │   0x00007f3b1c35bd09: popcnt 0x20(%r11,%rbx,8),%rdi
        //  0.02%  │││              │   0x00007f3b1c35bd10: popcnt 0x48(%r11,%rbx,8),%rdx
        // 12.86%  │││              │   0x00007f3b1c35bd17: popcnt 0x40(%r11,%rbx,8),%r9
        //  0.02%  │││              │   0x00007f3b1c35bd1e: popcnt 0x38(%r11,%rbx,8),%rax
        //  0.02%  │││              │   0x00007f3b1c35bd25: popcnt 0x30(%r11,%rbx,8),%rsi
        //         │││              │   0x00007f3b1c35bd2c: popcnt 0x18(%r11,%rbx,8),%r13
        //  7.61%  │││              │   0x00007f3b1c35bd33: popcnt 0x10(%r11,%rbx,8),%rbp
        //         │││              │   0x00007f3b1c35bd3a: add    %ecx,%ebp
        //  0.07%  │││              │   0x00007f3b1c35bd3c: add    %ebp,%r13d
        //  0.02%  │││              │   0x00007f3b1c35bd3f: add    %edi,%r13d
        //  8.25%  │││              │   0x00007f3b1c35bd42: add    %r8d,%r13d
        //  0.07%  │││              │   0x00007f3b1c35bd45: add    %r13d,%esi
        //  0.07%  │││              │   0x00007f3b1c35bd48: add    %esi,%eax
        //  7.94%  │││              │   0x00007f3b1c35bd4a: add    %eax,%r9d
        //  8.10%  │││              │   0x00007f3b1c35bd4d: add    %r9d,%edx
        int count = 0;
        for (long l : left) {
            count += Long.bitCount(l);
        }
        return count;
    }
}
