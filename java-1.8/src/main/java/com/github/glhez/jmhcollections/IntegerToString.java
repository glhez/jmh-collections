package com.github.glhez.jmhcollections;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Test various way to convert an {@code int} to {@link String}.
 *
 * @author gael.lhez
 *
 */
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Measurement(iterations = 100)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode({ Mode.AverageTime, Mode.SingleShotTime })
public class IntegerToString {

  @Param({ "" + Integer.MIN_VALUE, "" + Integer.MAX_VALUE, "0", "1000", "-1000" })
  private int n;

  @Benchmark
  public String integerToString() {
    return Integer.toString(n);
  }

  @Benchmark
  public String stringBuilder() {
    return new StringBuilder().append(n).toString();
  }

  @Benchmark
  public String emptyStringPlusInt() {
    return "" + n;
  }

  @Benchmark
  public String intPlusEmptyString() {
    return n + "";
  }

  @Benchmark
  public String stringFormat() {
    return String.format("%d", n);
  }
}
