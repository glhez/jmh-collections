package com.github.glhez.jmhcollections;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Warmup(  time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(  time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode({ Mode.AverageTime})
public class ComparatorBenchmark {

  @Param({ "" + Integer.MIN_VALUE, "" + Integer.MAX_VALUE, "10" /*, "-1000", "5000", "10000", "20000", "50000", "100000"*/ })
  private Integer v1;

  @Param({ "" + Integer.MIN_VALUE, "" + Integer.MAX_VALUE, "10" /*, "1000", "-5000", "10000", "20000", "50000", "100000" */ })
  private Integer v2;

  private Comparator<Integer> cmp1;
  private Comparator<Integer> cmp2;
  private Comparator<Integer> cmp3;

  @Setup
  public void setUp() {
    cmp1 = (a, b) -> Integer.compare(b, a);
    cmp2 = (a, b) -> b - a;
    cmp3 = Collections.reverseOrder();
  }

  @Benchmark
  public void with_Integer_compare(final Blackhole blackhole) {
    blackhole.consume(cmp1.compare(v1, v2));
  }

  @Benchmark
  public void with_b_minus_a(final Blackhole blackhole) {
    blackhole.consume(cmp2.compare(v1, v2));
  }

  @Benchmark
  public void with_reverse_comparator(final Blackhole blackhole) {
    blackhole.consume(cmp3.compare(v1, v2));
  }
}
