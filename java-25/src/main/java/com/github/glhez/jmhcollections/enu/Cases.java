package com.github.glhez.jmhcollections.enu;

import java.util.function.Supplier;

/**
 * We resort to this to avoid writing one benchmark per "number of enum" and to avoid reflection in the setup.
 * <p>
 * This may affect the benchmark (because we are not exactly testing enum as we should).
 *
 * @author gael.lhez
 */
public enum Cases implements Supplier<CodeProvider[]> {
  C3(SomeEnum3::values),
  C5(SomeEnum5::values),
  C7(SomeEnum7::values),
  C11(SomeEnum11::values),
  C13(SomeEnum13::values),
  C17(SomeEnum17::values),
  C19(SomeEnum19::values),
  C31(SomeEnum31::values),
  C37(SomeEnum37::values),
  C47(SomeEnum47::values),
  C53(SomeEnum53::values),
  C71(SomeEnum71::values);

  final Supplier<CodeProvider[]> values;

  Cases(final Supplier<CodeProvider[]> values) {
    this.values = values;
  }

  @Override
  public CodeProvider[] get() {
    return values.get();
  }

}