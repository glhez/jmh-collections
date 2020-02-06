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
      break

    fi
  done
}

benchmark './java-1.8/target/jmh-java-1.8-benchmark.jar' "${JAVA8_HOME}"  "${JAVA8_OPENJ9_HOME}" "${JAVA8_REDHAT_HOME}"
benchmark './java-11/target/jmh-java-11-benchmark.jar'   "${JAVA11_HOME}" "${JAVA11_OPENJ9_HOME}"
benchmark './java-13/target/jmh-java-13-benchmark.jar'   "${JAVA13_HOME}" "${JAVA13_OPENJ9_HOME}"
