# JMH Collection [![Build Status](https://travis-ci.org/glhez/jmh-collections.svg?branch=master)](https://travis-ci.org/glhez/jmh-collections)

A collection of JMH benchmark that I wanted to do, after reading question on StackOverflow (why not?).

Test is split per JDK, with a code duplication (heck) and specialization (eg: using new JDK feature) to ensure same code is compiled under the target JDK.



## `com.github.glhez.jmhcollections.LongBenchmark`

This will test the following case: filtering a value in a `List<Long>`, `long[]`, `Stream<Long>` or `LongStream`.

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

## `com.github.glhez.jmhcollections.IntegerToString`

This will test converting an `int` into a `String`.

There are many method (some may be counter intuitive or stupid:)):

- [`Integer.toString(int)`][2]
- [`new StringBuilder().append(int).toString();`][6]
- `"" + n` and `n + ""` (see [JLS 5.1.11][3] and [JKS 15.18.1][4])
- [`String.format("%d", n)`][5] (note: the resulting `String` is `Locale` dependent).

This test will behave differently on different JDK: "" + n use StringBuilder in Java 8 and some new class ([`java.lang.invoke.StringConcatFactory`][1])  introduced in Java 9.

[1]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/invoke/StringConcatFactory.html
[2]: https://docs.oracle.com/en/java/javase/11/docs/api/java/lang/Integer.html#toString-int-
[3]: https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.18.1
[4]: https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.11
[5]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#format(java.lang.String,java.lang.Object...)
[6]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuilder.html#append(int)
