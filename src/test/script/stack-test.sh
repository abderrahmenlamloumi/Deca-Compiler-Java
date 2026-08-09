#!/bin/bash

if [[ $# -ne 1 ]] || [[ ! -r "$1" ]]; then
  echo 'Missing assembly file to test' >&2
  exit 1
fi

for stackSize in {1..100}; do
  if ima -p "$stackSize" "$1" | grep ' IMA '; then
    echo "with stack size = $stackSize"
    break
  fi
done
