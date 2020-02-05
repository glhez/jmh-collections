package com.github.glhez.jmhcollections.enu;

public enum SomeEnum3 implements com.github.glhez.jmhcollections.EnumBenchmark.CodeProvider {
  VALUE0("@-value-0"),
  VALUE1("@-value-1"),
  VALUE2("@-value-2");
  
  private final String code;
  
  private SomeEnum3(String code) {
    this.code = code;
  }
  
  @Override
  public String getCode() {
    return this.code;
  }
}