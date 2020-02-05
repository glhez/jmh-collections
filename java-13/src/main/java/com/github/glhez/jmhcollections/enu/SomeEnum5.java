package com.github.glhez.jmhcollections.enu;

public enum SomeEnum5 implements com.github.glhez.jmhcollections.EnumBenchmark.CodeProvider {
  VALUE0("@-value-0"),
  VALUE1("@-value-1"),
  VALUE2("@-value-2"),
  VALUE3("@-value-3"),
  VALUE4("@-value-4");
  
  private final String code;
  
  private SomeEnum5(String code) {
    this.code = code;
  }
  
  @Override
  public String getCode() {
    return this.code;
  }
}