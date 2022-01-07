package dev.s1ck;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * https://richardstartin.github.io/posts/perf-myths-and-continuous-profiling#never-true-prefer-pre-increment-to-post-increment-for-loop-induction-variables
 */
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IncrementsBenchmark {

    @Param("1024")
    int size;

    private int[] input;
    private int[] output;

    @Setup(Level.Trial)
    public void setup() {
        input = ThreadLocalRandom.current().ints(size).toArray();
        output = new int[size];
    }

    // auto-vectorization
    // loop unrolling

    //....[Hottest Region 1]..............................................................................
    //c2, level 4, dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub, version 795 (413 bytes)
    //
    //                                                                ;*ifeq {reexecute=1 rethrow=0 return_oop=0}
    //                                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@30 (line 234)
    //                  0x00007f00884780c4: test   %eax,(%r10)        ;   {poll}
    //                  0x00007f00884780c7: test   %r11d,%r11d
    //                  0x00007f00884780ca: jne    0x00007f008847801b  ;*aload_1 {reexecute=0 rethrow=0 return_oop=0}
    //                                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@33 (line 235)
    //                  0x00007f00884780d0: mov    0x50(%rsp),%r10
    //                  0x00007f00884780d5: mov    0x10(%r10),%r8d    ;*getfield input {reexecute=0 rethrow=0 return_oop=0}
    //                                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@4 (line 204)
    //                                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //  0.46%           0x00007f00884780d9: mov    0xc(%r12,%r8,8),%r10d  ;*arraylength {reexecute=0 rethrow=0 return_oop=0}
    //                                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@7 (line 204)
    //                                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //                                                                ; implicit exception: dispatches to 0x00007f00884782f0
    //                  0x00007f00884780de: mov    0x50(%rsp),%r11
    //                  0x00007f00884780e3: mov    0x14(%r11),%r11d   ;*getfield output {reexecute=0 rethrow=0 return_oop=0}
    //                                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@12 (line 205)
    //                                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //                  0x00007f00884780e7: test   %r10d,%r10d
    //         ╭        0x00007f00884780ea: jbe    0x00007f0088478281  ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
    //         │                                                      ; - dev.s1ck.IncrementsBenchmark::autovecPre@8 (line 204)
    //         │                                                      ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         │        0x00007f00884780f0: mov    0xc(%r12,%r11,8),%r9d  ;*iaload {reexecute=0 rethrow=0 return_oop=0}
    //         │                                                      ; - dev.s1ck.IncrementsBenchmark::autovecPre@17 (line 205)
    //         │                                                      ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         │                                                      ; implicit exception: dispatches to 0x00007f008847828b
    //  0.01%  │        0x00007f00884780f5: test   %r9d,%r9d
    //         │╭       0x00007f00884780f8: jbe    0x00007f008847828b
    //         ││       0x00007f00884780fe: mov    %r10d,%ecx
    //         ││       0x00007f0088478101: dec    %ecx
    //  0.48%  ││       0x00007f0088478103: cmp    %r9d,%ecx
    //         ││╭      0x00007f0088478106: jae    0x00007f008847828b
    //         │││      0x00007f008847810c: cmp    %r10d,%ecx
    //         │││╭     0x00007f008847810f: jae    0x00007f008847828b
    //         ││││     0x00007f0088478115: lea    (%r12,%r8,8),%rdx
    //         ││││     0x00007f0088478119: lea    (%r12,%r11,8),%rsi
    //         ││││     0x00007f008847811d: mov    %esi,%r8d
    //  0.01%  ││││     0x00007f0088478120: shr    $0x2,%r8d
    //         ││││     0x00007f0088478124: and    $0x7,%r8d
    //         ││││     0x00007f0088478128: mov    $0x3,%ebx
    //  0.47%  ││││     0x00007f008847812d: sub    %r8d,%ebx
    //         ││││     0x00007f0088478130: and    $0x7,%ebx
    //         ││││     0x00007f0088478133: inc    %ebx
    //         ││││     0x00007f0088478135: cmp    %r10d,%ebx
    //         ││││     0x00007f0088478138: cmovg  %r10d,%ebx
    //         ││││     0x00007f008847813c: xor    %r9d,%r9d          ;*aload_0 {reexecute=0 rethrow=0 return_oop=0}
    //         ││││                                                   ; - dev.s1ck.IncrementsBenchmark::autovecPre@11 (line 205)
    //         ││││                                                   ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         ││││↗    0x00007f008847813f: mov    0x10(%rdx,%r9,4),%r8d
    //  0.87%  │││││    0x00007f0088478144: add    %r8d,0x10(%rsi,%r9,4)  ;*iastore {reexecute=0 rethrow=0 return_oop=0}
    //         │││││                                                  ; - dev.s1ck.IncrementsBenchmark::autovecPre@25 (line 205)
    //         │││││                                                  ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         │││││    0x00007f0088478149: inc    %r9d               ;*iinc {reexecute=0 rethrow=0 return_oop=0}
    //         │││││                                                  ; - dev.s1ck.IncrementsBenchmark::autovecPre@26 (line 204)
    //         │││││                                                  ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         │││││    0x00007f008847814c: cmp    %ebx,%r9d
    //  0.02%  ││││╰    0x00007f008847814f: jl     0x00007f008847813f  ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
    //         ││││                                                   ; - dev.s1ck.IncrementsBenchmark::autovecPre@8 (line 204)
    //         ││││                                                   ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         ││││     0x00007f0088478151: mov    %r10d,%r8d
    //  0.56%  ││││     0x00007f0088478154: add    $0xffffffc1,%r8d
    //         ││││     0x00007f0088478158: cmp    %r8d,%r9d
    //         ││││     0x00007f008847815b: jge    0x00007f008847807a
    //         ││││  ↗  0x00007f0088478161: mov    %r10d,%ebx
    //         ││││  │  0x00007f0088478164: sub    %r9d,%ebx
    //         ││││  │  0x00007f0088478167: add    $0xffffffc1,%ebx
    //  0.01%  ││││  │  0x00007f008847816a: mov    $0xfa00,%ecx
    //         ││││  │  0x00007f008847816f: cmp    %ecx,%ebx
    //         ││││  │  0x00007f0088478171: cmovg  %ecx,%ebx
    //  0.43%  ││││  │  0x00007f0088478174: add    %r9d,%ebx
    //         ││││  │  0x00007f0088478177: nopw   0x0(%rax,%rax,1)   ;*aload_0 {reexecute=0 rethrow=0 return_oop=0}
    //         ││││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@11 (line 205)
    //         ││││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         ││││ ↗│  0x00007f0088478180: vmovdqu 0x10(%rsi,%r9,4),%ymm0
    //  0.10%  ││││ ││  0x00007f0088478187: vpaddd 0x10(%rdx,%r9,4),%ymm0,%ymm0
    //  1.62%  ││││ ││  0x00007f008847818e: vmovdqu %ymm0,0x10(%rsi,%r9,4)
    //  5.56%  ││││ ││  0x00007f0088478195: vmovdqu 0x30(%rsi,%r9,4),%ymm0
    //  0.34%  ││││ ││  0x00007f008847819c: vpaddd 0x30(%rdx,%r9,4),%ymm0,%ymm0
    //  4.40%  ││││ ││  0x00007f00884781a3: vmovdqu %ymm0,0x30(%rsi,%r9,4)
    //  8.47%  ││││ ││  0x00007f00884781aa: vmovdqu 0x50(%rsi,%r9,4),%ymm0
    //  0.07%  ││││ ││  0x00007f00884781b1: vpaddd 0x50(%rdx,%r9,4),%ymm0,%ymm0
    //  1.25%  ││││ ││  0x00007f00884781b8: vmovdqu %ymm0,0x50(%rsi,%r9,4)
    //  4.98%  ││││ ││  0x00007f00884781bf: vmovdqu 0x70(%rsi,%r9,4),%ymm0
    //  0.21%  ││││ ││  0x00007f00884781c6: vpaddd 0x70(%rdx,%r9,4),%ymm0,%ymm0
    //  4.85%  ││││ ││  0x00007f00884781cd: vmovdqu %ymm0,0x70(%rsi,%r9,4)
    //  8.07%  ││││ ││  0x00007f00884781d4: vmovdqu 0x90(%rsi,%r9,4),%ymm0
    //  0.17%  ││││ ││  0x00007f00884781de: vpaddd 0x90(%rdx,%r9,4),%ymm0,%ymm0
    //  2.24%  ││││ ││  0x00007f00884781e8: vmovdqu %ymm0,0x90(%rsi,%r9,4)
    //  5.60%  ││││ ││  0x00007f00884781f2: vmovdqu 0xb0(%rsi,%r9,4),%ymm0
    //  0.31%  ││││ ││  0x00007f00884781fc: vpaddd 0xb0(%rdx,%r9,4),%ymm0,%ymm0
    //  4.52%  ││││ ││  0x00007f0088478206: vmovdqu %ymm0,0xb0(%rsi,%r9,4)
    //  8.28%  ││││ ││  0x00007f0088478210: vmovdqu 0xd0(%rsi,%r9,4),%ymm0
    //  0.13%  ││││ ││  0x00007f008847821a: vpaddd 0xd0(%rdx,%r9,4),%ymm0,%ymm0
    //  1.76%  ││││ ││  0x00007f0088478224: vmovdqu %ymm0,0xd0(%rsi,%r9,4)
    //  5.40%  ││││ ││  0x00007f008847822e: vmovdqu 0xf0(%rsi,%r9,4),%ymm0
    //  0.42%  ││││ ││  0x00007f0088478238: vpaddd 0xf0(%rdx,%r9,4),%ymm0,%ymm0
    //  4.57%  ││││ ││  0x00007f0088478242: vmovdqu %ymm0,0xf0(%rsi,%r9,4)  ;*iastore {reexecute=0 rethrow=0 return_oop=0}
    //         ││││ ││                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@25 (line 205)
    //         ││││ ││                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //  8.36%  ││││ ││  0x00007f008847824c: add    $0x40,%r9d         ;*iinc {reexecute=0 rethrow=0 return_oop=0}
    //         ││││ ││                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@26 (line 204)
    //         ││││ ││                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //  0.01%  ││││ ││  0x00007f0088478250: cmp    %ebx,%r9d
    //  0.48%  ││││ ╰│  0x00007f0088478253: jl     0x00007f0088478180  ;*goto {reexecute=0 rethrow=0 return_oop=0}
    //         ││││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@29 (line 204)
    //         ││││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         ││││  │  0x00007f0088478259: mov    0x108(%r15),%r14   ; ImmutableOopMap{r11=NarrowOop rdi=Oop rdx=Oop rsi=Oop [72]=Oop [80]=Oop [16]=Oop }
    //         ││││  │                                                ;*goto {reexecute=1 rethrow=0 return_oop=0}
    //         ││││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@29 (line 204)
    //         ││││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         ││││  │  0x00007f0088478260: test   %eax,(%r14)        ;*goto {reexecute=0 rethrow=0 return_oop=0}
    //         ││││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPre@29 (line 204)
    //         ││││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //         ││││  │                                                ;   {poll}
    //  0.42%  ││││  │  0x00007f0088478263: cmp    %r8d,%r9d
    //  0.05%  ││││  ╰  0x00007f0088478266: jl     0x00007f0088478161
    //         ││││     0x00007f008847826c: mov    %r10d,%r8d
    //         ││││     0x00007f008847826f: add    $0xfffffff9,%r8d
    //         ││││     0x00007f0088478273: cmp    %r8d,%r9d
    //  0.15%  ││││     0x00007f0088478276: jl     0x00007f008847805c
    //         ││││     0x00007f008847827c: jmpq   0x00007f008847807a
    //         ↘│││     0x00007f0088478281: mov    %rdi,0x40(%rsp)
    //          │││     0x00007f0088478286: jmpq   0x00007f0088478097  ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
    //          │││                                                   ; - dev.s1ck.IncrementsBenchmark::autovecPre@8 (line 204)
    //          │││                                                   ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPre_jmhTest::autovecPre_avgt_jmhStub@17 (line 232)
    //          ↘↘↘     0x00007f008847828b: mov    $0xffffff7e,%esi
    //                  0x00007f0088478290: mov    %rdi,0x40(%rsp)
    //                  0x00007f0088478295: mov    %r10d,0x18(%rsp)
    //                  0x00007f008847829a: xchg   %ax,%ax
    //                  0x00007f008847829c: vzeroupper
    //....................................................................................................
    // 86.12%  <total for region 1>
    @Benchmark
    public void autovecPre(Blackhole bh) {
        for (int i = 0; i < input.length; ++i) {
            output[i] += input[i];
        }
        bh.consume(output);
    }

    //....[Hottest Region 1]..............................................................................
    //c2, level 4, dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub, version 794 (329 bytes)
    //
    //  0.58%         0x00007f1400478a03: cmp    %r9d,%ecx
    //         ╭      0x00007f1400478a06: jae    0x00007f1400478b8b
    //         │      0x00007f1400478a0c: cmp    %r10d,%ecx
    //         │╭     0x00007f1400478a0f: jae    0x00007f1400478b8b
    //         ││     0x00007f1400478a15: lea    (%r12,%r8,8),%rdx
    //         ││     0x00007f1400478a19: lea    (%r12,%r11,8),%rsi
    //         ││     0x00007f1400478a1d: mov    %esi,%r8d
    //         ││     0x00007f1400478a20: shr    $0x2,%r8d
    //         ││     0x00007f1400478a24: and    $0x7,%r8d
    //         ││     0x00007f1400478a28: mov    $0x3,%ebx
    //  0.50%  ││     0x00007f1400478a2d: sub    %r8d,%ebx
    //         ││     0x00007f1400478a30: and    $0x7,%ebx
    //  0.01%  ││     0x00007f1400478a33: inc    %ebx
    //         ││     0x00007f1400478a35: cmp    %r10d,%ebx
    //         ││     0x00007f1400478a38: cmovg  %r10d,%ebx
    //         ││     0x00007f1400478a3c: xor    %r9d,%r9d          ;*aload_0 {reexecute=0 rethrow=0 return_oop=0}
    //         ││                                                   ; - dev.s1ck.IncrementsBenchmark::autovecPost@11 (line 182)
    //         ││                                                   ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         ││↗    0x00007f1400478a3f: mov    0x10(%rdx,%r9,4),%r8d
    //  0.36%  │││    0x00007f1400478a44: add    %r8d,0x10(%rsi,%r9,4)  ;*iastore {reexecute=0 rethrow=0 return_oop=0}
    //         │││                                                  ; - dev.s1ck.IncrementsBenchmark::autovecPost@25 (line 182)
    //         │││                                                  ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         │││    0x00007f1400478a49: inc    %r9d               ;*iinc {reexecute=0 rethrow=0 return_oop=0}
    //         │││                                                  ; - dev.s1ck.IncrementsBenchmark::autovecPost@26 (line 181)
    //         │││                                                  ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         │││    0x00007f1400478a4c: cmp    %ebx,%r9d
    //         ││╰    0x00007f1400478a4f: jl     0x00007f1400478a3f  ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
    //         ││                                                   ; - dev.s1ck.IncrementsBenchmark::autovecPost@8 (line 181)
    //         ││                                                   ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         ││     0x00007f1400478a51: mov    %r10d,%r8d
    //  0.36%  ││     0x00007f1400478a54: add    $0xffffffc1,%r8d
    //         ││     0x00007f1400478a58: cmp    %r8d,%r9d
    //         ││     0x00007f1400478a5b: jge    0x00007f140047897a
    //         ││  ↗  0x00007f1400478a61: mov    %r10d,%ebx
    //         ││  │  0x00007f1400478a64: sub    %r9d,%ebx
    //         ││  │  0x00007f1400478a67: add    $0xffffffc1,%ebx
    //         ││  │  0x00007f1400478a6a: mov    $0xfa00,%ecx
    //         ││  │  0x00007f1400478a6f: cmp    %ecx,%ebx
    //         ││  │  0x00007f1400478a71: cmovg  %ecx,%ebx
    //  0.47%  ││  │  0x00007f1400478a74: add    %r9d,%ebx
    //         ││  │  0x00007f1400478a77: nopw   0x0(%rax,%rax,1)   ;*aload_0 {reexecute=0 rethrow=0 return_oop=0}
    //         ││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPost@11 (line 182)
    //         ││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         ││ ↗│  0x00007f1400478a80: vmovdqu 0x10(%rsi,%r9,4),%ymm0
    //  0.32%  ││ ││  0x00007f1400478a87: vpaddd 0x10(%rdx,%r9,4),%ymm0,%ymm0
    //  5.22%  ││ ││  0x00007f1400478a8e: vmovdqu %ymm0,0x10(%rsi,%r9,4)
    //  9.70%  ││ ││  0x00007f1400478a95: vmovdqu 0x30(%rsi,%r9,4),%ymm0
    //  0.05%  ││ ││  0x00007f1400478a9c: vpaddd 0x30(%rdx,%r9,4),%ymm0,%ymm0
    //  1.28%  ││ ││  0x00007f1400478aa3: vmovdqu %ymm0,0x30(%rsi,%r9,4)
    //  4.38%  ││ ││  0x00007f1400478aaa: vmovdqu 0x50(%rsi,%r9,4),%ymm0
    //  0.20%  ││ ││  0x00007f1400478ab1: vpaddd 0x50(%rdx,%r9,4),%ymm0,%ymm0
    //  4.72%  ││ ││  0x00007f1400478ab8: vmovdqu %ymm0,0x50(%rsi,%r9,4)
    //  7.59%  ││ ││  0x00007f1400478abf: vmovdqu 0x70(%rsi,%r9,4),%ymm0
    //  0.12%  ││ ││  0x00007f1400478ac6: vpaddd 0x70(%rdx,%r9,4),%ymm0,%ymm0
    //  2.10%  ││ ││  0x00007f1400478acd: vmovdqu %ymm0,0x70(%rsi,%r9,4)
    //  4.48%  ││ ││  0x00007f1400478ad4: vmovdqu 0x90(%rsi,%r9,4),%ymm0
    //  0.43%  ││ ││  0x00007f1400478ade: vpaddd 0x90(%rdx,%r9,4),%ymm0,%ymm0
    //  4.97%  ││ ││  0x00007f1400478ae8: vmovdqu %ymm0,0x90(%rsi,%r9,4)
    //  7.79%  ││ ││  0x00007f1400478af2: vmovdqu 0xb0(%rsi,%r9,4),%ymm0
    //  0.17%  ││ ││  0x00007f1400478afc: vpaddd 0xb0(%rdx,%r9,4),%ymm0,%ymm0
    //  1.43%  ││ ││  0x00007f1400478b06: vmovdqu %ymm0,0xb0(%rsi,%r9,4)
    //  4.31%  ││ ││  0x00007f1400478b10: vmovdqu 0xd0(%rsi,%r9,4),%ymm0
    //  0.60%  ││ ││  0x00007f1400478b1a: vpaddd 0xd0(%rdx,%r9,4),%ymm0,%ymm0
    //  5.47%  ││ ││  0x00007f1400478b24: vmovdqu %ymm0,0xd0(%rsi,%r9,4)
    //  8.91%  ││ ││  0x00007f1400478b2e: vmovdqu 0xf0(%rsi,%r9,4),%ymm0
    //  0.07%  ││ ││  0x00007f1400478b38: vpaddd 0xf0(%rdx,%r9,4),%ymm0,%ymm0
    //  1.61%  ││ ││  0x00007f1400478b42: vmovdqu %ymm0,0xf0(%rsi,%r9,4)  ;*iastore {reexecute=0 rethrow=0 return_oop=0}
    //         ││ ││                                                ; - dev.s1ck.IncrementsBenchmark::autovecPost@25 (line 182)
    //         ││ ││                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //  4.35%  ││ ││  0x00007f1400478b4c: add    $0x40,%r9d         ;*iinc {reexecute=0 rethrow=0 return_oop=0}
    //         ││ ││                                                ; - dev.s1ck.IncrementsBenchmark::autovecPost@26 (line 181)
    //         ││ ││                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //  0.17%  ││ ││  0x00007f1400478b50: cmp    %ebx,%r9d
    //  0.28%  ││ ╰│  0x00007f1400478b53: jl     0x00007f1400478a80  ;*goto {reexecute=0 rethrow=0 return_oop=0}
    //         ││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPost@29 (line 181)
    //         ││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         ││  │  0x00007f1400478b59: mov    0x108(%r15),%r14   ; ImmutableOopMap{r11=NarrowOop rdi=Oop rdx=Oop rsi=Oop [72]=Oop [80]=Oop [16]=Oop }
    //         ││  │                                                ;*goto {reexecute=1 rethrow=0 return_oop=0}
    //         ││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPost@29 (line 181)
    //         ││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //  0.02%  ││  │  0x00007f1400478b60: test   %eax,(%r14)        ;*goto {reexecute=0 rethrow=0 return_oop=0}
    //         ││  │                                                ; - dev.s1ck.IncrementsBenchmark::autovecPost@29 (line 181)
    //         ││  │                                                ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         ││  │                                                ;   {poll}
    //  0.50%  ││  │  0x00007f1400478b63: cmp    %r8d,%r9d
    //  0.10%  ││  ╰  0x00007f1400478b66: jl     0x00007f1400478a61
    //         ││     0x00007f1400478b6c: mov    %r10d,%r8d
    //         ││     0x00007f1400478b6f: add    $0xfffffff9,%r8d
    //  0.04%  ││     0x00007f1400478b73: cmp    %r8d,%r9d
    //  0.03%  ││     0x00007f1400478b76: jl     0x00007f140047895c
    //         ││     0x00007f1400478b7c: jmpq   0x00007f140047897a
    //         ││     0x00007f1400478b81: mov    %rdi,0x40(%rsp)
    //         ││     0x00007f1400478b86: jmpq   0x00007f1400478997  ;*if_icmpge {reexecute=0 rethrow=0 return_oop=0}
    //         ││                                                   ; - dev.s1ck.IncrementsBenchmark::autovecPost@8 (line 181)
    //         ││                                                   ; - dev.s1ck.jmh_generated.IncrementsBenchmark_autovecPost_jmhTest::autovecPost_avgt_jmhStub@17 (line 232)
    //         ↘↘     0x00007f1400478b8b: mov    $0xffffff7e,%esi
    //                0x00007f1400478b90: mov    %rdi,0x40(%rsp)
    //                0x00007f1400478b95: mov    %r10d,0x18(%rsp)
    //                0x00007f1400478b9a: xchg   %ax,%ax
    //                0x00007f1400478b9c: vzeroupper
    //....................................................................................................
    // 83.15%  <total for region 1>
    @Benchmark
    public void autovecPost(Blackhole bh) {
        for (int i = 0; i < input.length; i++) {
            output[i] += input[i];
        }
        bh.consume(output);
    }

    // no auto-vectorization
    // loop unrolling

    @Benchmark
    public int reducePre(Blackhole bh) {
        int sum = 0;
        for (int i = 0; i < input.length; ++i) {
            sum += Integer.bitCount(input[i]);
        }
        return sum;
    }

    @Benchmark
    public int reducePost(Blackhole bh) {
        int sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += Integer.bitCount(input[i]);
        }
        return sum;
    }

    // no auto-vectorization
    // no loop unrolling

    @Benchmark
    public void blackholedPre(Blackhole bh) {
        for (int i = 0; i < input.length; ++i) {
            bh.consume(i);
        }
    }

    @Benchmark
    public void blackholedPost(Blackhole bh) {
        for (int i = 0; i < input.length; i++) {
            bh.consume(i);
        }
    }

}
