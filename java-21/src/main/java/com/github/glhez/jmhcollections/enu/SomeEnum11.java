package com.github.glhez.jmhcollections.enu;

public enum SomeEnum11 implements CodeProvider {
  VALUE0("@-value-0"),
  VALUE1("@-value-1"),
  VALUE2("@-value-2"),
  VALUE3("@-value-3"),
  VALUE4("@-value-4"),
  VALUE5("@-value-5"),
  VALUE6("@-value-6"),
  VALUE7("@-value-7"),
  VALUE8("@-value-8"),
  VALUE9("@-value-9"),
  VALUE10("@-value-10");

  private final String code;

  private SomeEnum11(String code) {
    this.code = code;
  }

  @Override
  public String getCode() {
    return this.code;
  }
}