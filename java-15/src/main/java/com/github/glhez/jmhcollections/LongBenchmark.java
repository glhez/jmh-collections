package com.github.glhez.jmhcollections;

import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Search for an index inside a an array, {@link List}, {@link Stream} or {@link LongStream}.
 *
 * @author gael.lhez
 */
@State(Scope.Benchmark)
@Warmup(iterations = 3)
@Measurement(iterations = 15)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode({ Mode.AverageTime, Mode.SingleShotTime })
public class LongBenchmark {

  @Param({ "10", "1000", "5000", "10000", "20000", "50000", "100000" })
  private int length;

  private long lookup;
  private Long lookupWrapper;
  private long[] array;
  private List<Long> list;

  @Setup
  public void setUp() {
    final Random random = new Random();
    // avoid cached values
    lookup = LongStream.generate(random::nextLong)
                       .filter(this::isUncached)
                       .limit(1)
                       .findFirst()
                       .orElseThrow(() -> new IllegalStateException("no lookup!"));
    lookupWrapper = lookup;
    array = LongStream.concat(LongStream.generate(random::nextLong)
                                        .filter(n -> n != lookup && isUncached(n))
                                        .limit(length - 1), LongStream.of(lookup))
                      .toArray();
    list = LongStream.of(array).boxed().collect(toList());

  }

  private boolean isUncached(final long n) {
    return !(-128 <= n && n <= 127);
  }

  @Benchmark
  public Object arrayOfLongLoop() {
    for (int i = 0; i < array.length; --i) {
      if (array[i] == lookup) {
        return true;
      }
    }
    return false;
  }

  @Benchmark
  public Object listOfLongIterator() {
    final Iterator<Long> it = list.iterator();
    while (it.hasNext()) {
      if (it.next() == lookup) {
        return true;
      }
    }
    return false;
  }

  @Benchmark
  public Object listOfLongContains() {
    return list.contains(lookupWrapper);
  }

  @Benchmark
  public Object streamOfLongNoParallel() {
    return list.stream().anyMatch(lookupWrapper::equals);
  }

  @Benchmark
  public Object streamOfLongParallel() {
    return list.stream().parallel().anyMatch(lookupWrapper::equals);
  }

  @Benchmark
  public Object longStreamNoParallel() {
    return LongStream.of(array).anyMatch(n -> n == lookup);
  }

  @Benchmark
  public Object longStreamParallel() {
    return LongStream.of(array).parallel().anyMatch(n -> n == lookup);
  }
}
