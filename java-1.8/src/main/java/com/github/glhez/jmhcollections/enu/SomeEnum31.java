package com.github.glhez.jmhcollections.enu;

public enum SomeEnum31 implements com.github.glhez.jmhcollections.EnumBenchmark.CodeProvider {
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
  VALUE10("@-value-10"),
  VALUE11("@-value-11"),
  VALUE12("@-value-12"),
  VALUE13("@-value-13"),
  VALUE14("@-value-14"),
  VALUE15("@-value-15"),
  VALUE16("@-value-16"),
  VALUE17("@-value-17"),
  VALUE18("@-value-18"),
  VALUE19("@-value-19"),
  VALUE20("@-value-20"),
  VALUE21("@-value-21"),
  VALUE22("@-value-22"),
  VALUE23("@-value-23"),
  VALUE24("@-value-24"),
  VALUE25("@-value-25"),
  VALUE26("@-value-26"),
  VALUE27("@-value-27"),
  VALUE28("@-value-28"),
  VALUE29("@-value-29"),
  VALUE30("@-value-30");
  
  private final String code;
  
  private SomeEnum31(String code) {
    this.code = code;
  }
  
  @Override
  public String getCode() {
    return this.code;
  }
}