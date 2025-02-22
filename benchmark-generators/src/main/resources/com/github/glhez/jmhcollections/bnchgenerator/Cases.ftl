package ${packageName};

import java.util.function.Supplier;

/**
 * We resort to this to avoid writing one benchmark per "number of enum" and to avoid reflection in the setup.
 * <p>
 * This may affect the benchmark (because we are not exactly testing enum as we should).
 *
 * @author gael.lhez
 */
public enum ${className} implements Supplier<CodeProvider[]> {
<#list values as value>  
  C${value.numberOfConstants}(${value.enumClassName}::values)<#sep>,
</#sep></#list>
  ;

  final Supplier<CodeProvider[]> values;

  Cases(final Supplier<CodeProvider[]> values) {
    this.values = values;
  }

  @Override
  public CodeProvider[] get() {
    return values.get();
  }

}