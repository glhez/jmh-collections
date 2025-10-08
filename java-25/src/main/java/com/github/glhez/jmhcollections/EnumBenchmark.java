package com.github.glhez.jmhcollections;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

import com.github.glhez.jmhcollections.enu.Cases;
import com.github.glhez.jmhcollections.enu.CodeProvider;

/**
 * Test various way to convert
 *
 * @author gael.lhez
 */
@State(Scope.Benchmark)
@Warmup(iterations = 3)
@Measurement(iterations = 15)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode({ Mode.AverageTime, Mode.SingleShotTime })
public class EnumBenchmark {

  @Param
  private Cases enumCase;

  private CodeProvider someValue;
  private CodeProvider[] cachedValues;
  private Map<String, CodeProvider> mapping;

  @Setup
  public void setUp() {
    cachedValues = enumCase.get();
    someValue = cachedValues[cachedValues.length - 1];
    mapping = createMapping(cachedValues, CodeProvider::getCode);
  }

  private Map<String, CodeProvider> createMapping(final CodeProvider[] values,
      final Function<CodeProvider, String> functor) {
    Objects.requireNonNull(values, "values");
    Objects.requireNonNull(functor, "functor");
    final Map<String, CodeProvider> result = new HashMap<>();
    for (final CodeProvider provider : values) {
      result.put(functor.apply(provider), provider);
    }
    if (values.length != result.size()) {
      throw new IllegalStateException("expected same number of values in map; some code key is wrong");
    }

    return Collections.unmodifiableMap(result);
  }

  @Benchmark
  public CodeProvider usingAMap() {
    return mapping.get(someValue.getCode());
  }

  @Benchmark
  public CodeProvider usingValues() {
    final String look = someValue.getCode();
    for (final CodeProvider provider : enumCase.get()) {
      if (look.equals(provider.getCode())) {
        return provider;
      }
    }
    return null;
  }

  @Benchmark
  public CodeProvider usingConstantValues() {
    final String look = someValue.getCode();
    for (final CodeProvider provider : cachedValues) {
      if (look.equals(provider.getCode())) {
        return provider;
      }
    }
    return null;
  }

}
