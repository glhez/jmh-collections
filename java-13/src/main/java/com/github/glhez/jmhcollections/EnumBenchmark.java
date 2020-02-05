package com.github.glhez.jmhcollections;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
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

import com.github.glhez.jmhcollections.enu.SomeEnum11;
import com.github.glhez.jmhcollections.enu.SomeEnum13;
import com.github.glhez.jmhcollections.enu.SomeEnum17;
import com.github.glhez.jmhcollections.enu.SomeEnum19;
import com.github.glhez.jmhcollections.enu.SomeEnum3;
import com.github.glhez.jmhcollections.enu.SomeEnum31;
import com.github.glhez.jmhcollections.enu.SomeEnum37;
import com.github.glhez.jmhcollections.enu.SomeEnum47;
import com.github.glhez.jmhcollections.enu.SomeEnum5;
import com.github.glhez.jmhcollections.enu.SomeEnum53;
import com.github.glhez.jmhcollections.enu.SomeEnum7;
import com.github.glhez.jmhcollections.enu.SomeEnum71;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

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

  private static final String PACKAGE_NAME = EnumBenchmark.class.getPackage().getName() + ".enu";
  private static final String CLASS_NAME_PATTERN = "SomeEnum%s";

  @Param({ "C3", "C5", "C7", "C11", "C13", "C17", "C19", "C31", "C37", "C47", "C53", "C71" })
  private Cases enumCase;

  private CodeProvider someValue;
  private CodeProvider[] cachedValues;
  private Map<String, CodeProvider> mapping;

  @Setup
  public void setUp() {
    cachedValues = enumCase.values.get();
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
    for (final CodeProvider provider : enumCase.values.get()) {
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

  /**
   * We resort to this to avoid writing one benchmark per "number of enum" and to avoid reflection in the setup.
   * <p>
   * This may affect the benchmark (because we are not exactly testing enum as we should).
   *
   * @author gael.lhez
   */
  enum Cases {
    C3(3, SomeEnum3::values),
    C5(5, SomeEnum5::values),
    C7(7, SomeEnum7::values),
    C11(11, SomeEnum11::values),
    C13(13, SomeEnum13::values),
    C17(17, SomeEnum17::values),
    C19(19, SomeEnum19::values),
    C31(31, SomeEnum31::values),
    C37(37, SomeEnum37::values),
    C47(47, SomeEnum47::values),
    C53(53, SomeEnum53::values),
    C71(71, SomeEnum71::values);

    private final int length;
    private final Supplier<CodeProvider[]> values;

    Cases(final int length, final Supplier<CodeProvider[]> values) {
      this.length = length;
      this.values = values;
    }

  }

  public static void main(final String[] args) throws IOException, TemplateException {
    final Path src = createEnumDirectories(Paths.get("src/main/java").resolve(PACKAGE_NAME.replace('.', '/')));
    final Configuration cfg = createConfiguration();
    final Template template = cfg.getTemplate("EnumBenchmark.ftl");

    for (final int n : Arrays.stream(Cases.values()).mapToInt(c -> c.length).toArray()) {
      final String className = String.format(CLASS_NAME_PATTERN, n);
      final Path file = src.resolve(className + ".java");
      final Map<String, Object> data = createDataModel(PACKAGE_NAME, className, n);

      try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
        template.process(data, writer);
      }
    }
  }

  private static Map<String, Object> createDataModel(final String targetPackage, final String className, final int n) {
    final Map<String, Object> data = new HashMap<>();
    data.put("packageName", targetPackage);
    data.put("className", className);
    data.put("values", IntStream.range(0, n).mapToObj(EnumBenchmark::createEnumValueMap).collect(toList()));
    return data;
  }

  private static Map<String, Object> createEnumValueMap(final int i) {
    final Map<String, Object> map = new HashMap<>();
    map.put("name", "VALUE" + i);
    map.put("code", "@-value-" + i);
    return map;
  }

  private static Path createEnumDirectories(final Path src) throws IOException {
    if (Files.isDirectory(src)) {
      try (final Stream<Path> ss = Files.find(src, Integer.MAX_VALUE, (path, attr) -> attr.isRegularFile())) {
        ss.forEach(file -> {
          try {
            Files.deleteIfExists(file);
          } catch (final IOException e) {
            e.printStackTrace();
          }
        });
      }
    } else {
      Files.createDirectories(src);
    }
    return src;
  }

  private static Configuration createConfiguration() {
    final Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
    cfg.setClassForTemplateLoading(EnumBenchmark.class, "");
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setFallbackOnNullLoopVariable(false);
    return cfg;
  }

  public interface CodeProvider {
    String getCode();
  }

}
