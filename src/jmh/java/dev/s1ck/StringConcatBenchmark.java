package dev.s1ck;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringConcatBenchmark {

    public static final String FOO = "foo";
    public static final String BAR = "bar";

    @Benchmark
    public String concatenate() {
        return concatenateString(FOO, BAR);
    }

    @Benchmark
    public String stringBuilder() {
        return concatenateStringBuilder(FOO, BAR);
    }

    @Benchmark
    public String format() {
        return formatString(FOO, BAR);
    }

    // static java.lang.String concatenateString(java.lang.String, java.lang.String);
    //    Code:
    //       0: aload_0
    //       1: aload_1
    //       2: invokedynamic #8,  0              // InvokeDynamic #0:makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //       7: areturn
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    static String concatenateString(String left, String right) {
        return left + right;
    }

    // static java.lang.String concatenateStringBuilder(java.lang.String, java.lang.String);
    //    Code:
    //       0: new           #9                  // class java/lang/StringBuilder
    //       3: dup
    //       4: invokespecial #10                 // Method java/lang/StringBuilder."<init>":()V
    //       7: aload_0
    //       8: invokevirtual #11                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //      11: aload_1
    //      12: invokevirtual #11                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //      15: invokevirtual #12                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
    //      18: areturn
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    static String concatenateStringBuilder(String left, String right) {
        return new StringBuilder()
                .append(left).append(right)
                .toString();
    }

    // static java.lang.String formatString(java.lang.String, java.lang.String);
    //    Code:
    //       0: ldc           #13                 // String %s%s
    //       2: iconst_2
    //       3: anewarray     #14                 // class java/lang/Object
    //       6: dup
    //       7: iconst_0
    //       8: aload_0
    //       9: aastore
    //      10: dup
    //      11: iconst_1
    //      12: aload_1
    //      13: aastore
    //      14: invokestatic  #15                 // Method java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //      17: areturn
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    static String formatString(String left, String right) {
        return String.format("%s%s", left, right);
    }
}
