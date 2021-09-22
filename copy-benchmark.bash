#!/bin/bash

# resource should be shared, java are tied to how the JDK compile file
copy_files() {
  local source="$1"
  shift

  while read -r file; do
    local suffix="${file#*/}"

    echo ">>> copying ${file} to target..."
    for target; do
      local dest="java-${target}/${suffix}"
      local dest_dir="${dest%/*}"

      ( [[ -d "$dest_dir" ]] || mkdir -pv "${dest_dir}" ) && cp -v "$file" "$dest"

    done
  done < <(find "java-${source}/src/main/java" "java-${source}/src/main/resources" -type f )
}



copy_files 1.8 11 17

