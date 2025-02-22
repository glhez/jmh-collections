package ${packageName};

public enum ${className} implements CodeProvider {
<#list values as value>  ${value.name}("${value.code}")<#sep>,
</#sep></#list>;
  
  private final String code;
  
  private ${className}(String code) {
    this.code = code;
  }
  
  @Override
  public String getCode() {
    return this.code;
  }
}