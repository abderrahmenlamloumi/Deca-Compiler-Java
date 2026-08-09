#!/bin/bash

if command -v deca &> /dev/null; then
    echo "Error, executable \"decac\" not found in PATH."
    exit 1
fi

nbtests=0
nbpassed=0

pushd src/test/deca/context/invalid/devinetteDichotomique > /dev/null || exit 1
for source in *.deca; do
  output="${source%.deca}.ass"
  res="${source%.deca}.res"
  expected="${source%.deca}.expected"
  ((nbtests++))
  if ! decac "$source" 2> "$res"; then
    if grep -q "$(< "$expected")" "$res"; then
      echo "--- $source: PASSED ---"
      ((nbpassed++))
      continue
    fi
  fi
  echo "--- $source: KO ---"
done
popd > /dev/null || exit 1

pushd src/test/deca/codegen/valid/devinetteDichotomique > /dev/null || exit 1
for source in *.deca; do
  output="${source%.deca}.ass"
  expected="${source%.deca}.expected"
  ((nbtests++))
  if decac "$source"; then
    if diff "$expected" <(ima "$output"); then
      echo "--- $source: PASSED ---"
      ((nbpassed++))
    else
      echo "--- $source: KO ---"
    fi
  fi
done
popd > /dev/null || exit 1

pushd src/test/deca/codegen/invalid/devinetteDichotomique > /dev/null || exit 1
for source in *.deca; do
  output="${source%.deca}.ass"
  expected="${source%.deca}.expected"
  ((nbtests++))
  if decac "$source"; then
    if diff "$expected" <(ima "$output"); then
      echo "--- $source: PASSED ---"
      ((nbpassed++))
    else
      echo "--- $source: KO ---"
    fi
  fi
done
popd > /dev/null || exit 1

pushd src/test/deca/codegen/interactive/devinetteDichotomique > /dev/null || exit 1
for source in *.deca; do
  output="${source%.deca}.ass"
  expected="${source%.deca}.expected"
  input="${source%.deca}.in"
  ((nbtests++))
  if decac "$source"; then
    if diff "$expected" <(ima "$output" < "$input"); then
      echo "--- $source: PASSED ---"
      ((nbpassed++))
    else
      echo "--- $source: KO ---"
    fi
  fi
done
popd > /dev/null || exit 1

echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"
