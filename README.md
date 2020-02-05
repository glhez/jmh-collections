# JMH Collection [![Build Status](https://travis-ci.org/glhez/jmh-collections.svg?branch=master)](https://travis-ci.org/glhez/jmh-collections)

A collection of JMH benchmark that I wanted to do, after reading question on StackOverflow (why not?).

Test is split per JDK, with a code duplication (heck) and specialization (eg: using new JDK feature) to ensure same code is compiled under the target JDK.

# Running the benchmark

This may take a lot of times!

# Writing a benchmark

Benchmark are copied from Java 8 project up to 11 and 13, using `copy-benchmark.bash`.

Since the JDK evolve, compiled bytecode may differ (for example, Java 8 use [`StringBuilder`][6] whereas Java 9 use [`StringConcatFactory`][1] and API may evolve.

Naming of benchmark:

- Benchmark class must end with `Benchmark`
- Benchmark for specific **major** version of Java must end with the target release, such as `Benchmark8`, `Benchmark11` and `Benchmark13` for Java 1.8, 11 and 13.


# The Benchmark

## `LongBenchmark`

This will test the following case: filtering a value in a `List<Long>`, `long[]`, `Stream<Long>` or [`LongStream`][7].

Setup will:

- Create a `lookup`
- Create a `long[]` array of N-1 items, not containing `Long` cached values and `lookup`.
- Create a `List<Long>` based on array.

Test will:

- Search in array using a `for` loop.
- Search in list using `List.iterator()`
- Search in list using `List.contains(E)`.
- Search using `Stream<Long>.anyMatch(Predicate<Long>)`
- Parallel search using `Stream<Long>.anyMatch(Predicate<Long>)`
- Search using `LongStream.anyMatch(LongPredicate)`
- Parallel search using `LongStream.anyMatch(LongPredicate)`

## `IntegerToStringBenchmark`

This will test converting an `int` into a `String`.

There are many method (some may be counter intuitive or stupid:)):

- [`Integer.toString(int)`][2]
- [`new StringBuilder().append(int).toString();`][6]
- `"" + n` and `n + ""` (see [JLS 5.1.11][3] and [JKS 15.18.1][4])
- [`String.format("%d", n)`][5] (note: the resulting `String` is `Locale` dependent).

This test will behave differently on different JDK: "" + n use StringBuilder in Java 8 and some new class ([`java.lang.invoke.StringConcatFactory`][1])  introduced in Java 9.

## `EnumBenchmark`

This comes from a question: should we use map to find an enum by some random value? For example, let say that for each enum A we have a `String` identifier whose content is invalid with Java identifier:

- Should we use a `Map<String, E>` ?
- Should we iterate over `E.values()` ?
- Do we cache `values()` ?

The benchmark enum with multiple cardinality in order to see when one these become "best at".



[1]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/invoke/StringConcatFactory.html
[2]: https://docs.oracle.com/en/java/javase/11/docs/api/java/lang/Integer.html#toString-int-
[3]: https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.18.1
[4]: https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.11
[5]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#format(java.lang.String,java.lang.Object...)
[6]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuilder.html#append(int)
[7]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/LongStream.html
