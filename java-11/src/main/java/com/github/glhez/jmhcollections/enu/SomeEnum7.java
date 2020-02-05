package com.github.glhez.jmhcollections.enu;

public enum SomeEnum7 implements com.github.glhez.jmhcollections.EnumBenchmark.CodeProvider {
  VALUE0("@-value-0"),
  VALUE1("@-value-1"),
  VALUE2("@-value-2"),
  VALUE3("@-value-3"),
  VALUE4("@-value-4"),
  VALUE5("@-value-5"),
  VALUE6("@-value-6");
  
  private final String code;
  
  private SomeEnum7(String code) {
    this.code = code;
  }
  
  @Override
  public String getCode() {
    return this.code;
  }
}