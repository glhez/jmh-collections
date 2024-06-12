#!/bin/bash

benchmark() {
  local jar=$1
  shift
  for jdk_home; do
    local java_exec="${jdk_home}/bin/java"

    if [[ -x "${java_exec}" ]]; then
      echo "running using ${jdk_home}"

      local -A properties=()
      while read -r property_line; do
        local key="${property_line%% = *}"
        local value="${property_line#* = }"
        properties["$key"]="$value"
      done < <( "${java_exec}" -XshowSettings:properties -version 2>&1 | grep -Po 'java.(vendor|version) = .+' )

      # not all JDK returns the valid vendor, help them
      if [[ "${jdk_home,,}" =~ redhat ]]; then
        properties[java.vendor]=RedHat
      fi

      for key in "${!properties[@]}"; do
        echo ":: ${key}: ${properties[$key]}"
      done

      "${java_exec}" -jar "$jar" -rf json
    fi
  done
}

benchmark './java-1.8/target/jmh-java-1.8-benchmark.jar' "${JAVA_8_HOME}"
benchmark './java-17/target/jmh-java-17-benchmark.jar'   "${JAVA_17_HOME}"
benchmark './java-21/target/jmh-java-21-benchmark.jar'   "${JAVA_21_HOME}"
