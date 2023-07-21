#!/bin/bash

FILES=(
  "CODE_OF_CONDUCT.md"
  "CODE_OF_CONDUCT_de.md"
  "pom.xml"
  "README.adoc"
  ".gitignore"
  "LICENSE.txt"
  "src/test/java/com/soebes/module/calculator/ChecksumForFileResult.java"
  "src/test/java/com/soebes/module/calculator/ChecksumResult.java"
  "src/test/java/com/soebes/module/calculator/CalculateChecksum.java"
  "src/test/java/com/soebes/module/calculator/ByteArrayWrapperTest.java"
  "src/test/java/com/soebes/module/calculator/ModulCalculatorTest.java"
  "src/main/java/com/soebes/module/calculator/Main.java"
  "src/main/java/com/soebes/module/calculator/ByteArrayWrapper.java"
)

for i in ${FILES[@]}; do
  shasum -a 256 $i;
done;
