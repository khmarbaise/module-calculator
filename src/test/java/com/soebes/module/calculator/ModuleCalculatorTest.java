package com.soebes.module.calculator;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Karl Heinz Marbaise
 */
class ModuleCalculatorTest {

  private static final List<String> DEFAULT_EXCLUDES = Arrays.asList("target", ".git", ".github", ".idea");

  /**
   * Defined as int array instead of byte array otherwise
   * needed a lot of casts.
   */
  private static byte[] convert() {
    int[] given = new int[]{
        0x6e, 0x55, 0x00, 0xcf, 0x09, 0x33, 0x8d,
        0x47, 0xff, 0xb6, 0x89, 0x9d, 0xb0, 0xb0,
        0x58, 0x43, 0x1d, 0xaa, 0xa2, 0x61, 0xfa,
        0xc2, 0xc2, 0x3c, 0x56, 0x82, 0xaf, 0x4c,
        0xb1, 0x64, 0x3b, 0x94
    };

    byte[] byteArray = new byte[given.length];

    for (int i = 0; i < given.length; i++) {
      byteArray[i] = (byte)given[i];
    }

    return byteArray;
  }

  static final ChecksumForFileResult DEFAULT = new ChecksumForFileResult(convert());


  @Test
  void checkToCalculateTheDefaultHashSum() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build()) {
      Path root = fileSystem.getPath("/");
      Files.createDirectories(root);
      Files.createDirectories(root.resolve("/root-project/src/main/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/src/test/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/target/maven-status"));

      Files.write(root.resolve("/root-project/src/main/java/com/example/FirstJava.java"),"Das ist ein Test".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/pom.xml"), "pom of the project.".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/target/maven-status/module.hash"), "hashfile".getBytes(StandardCharsets.UTF_8)); // Will be ignored!
      ModuleCalculator moduleCalculator = new ModuleCalculator();
      ChecksumForFileResult result = moduleCalculator.calculateHashForDirectoryTree(root.resolve("/root-project"), DEFAULT_EXCLUDES);

      assertThat(result).isEqualTo(DEFAULT);
    }
  }

  @Test
  void hasChangedShouldBeFalse() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build()) {
      Path root = fileSystem.getPath("/");
      Files.createDirectories(root);
      Files.createDirectories(root.resolve("/root-project/src/main/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/src/test/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/target/maven-status"));

      Files.write(root.resolve("/root-project/src/main/java/com/example/FirstJava.java"),"Das ist ein Test".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/pom.xml"), "pom of the project.".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/target/maven-status/module.hash"), convert());
      ModuleCalculator moduleCalculator = new ModuleCalculator();
      boolean hashChanged = moduleCalculator.hashChanged(root.resolve("/root-project"), root.resolve("/root-project/target/maven-status/module.hash"), DEFAULT_EXCLUDES);

      assertThat(hashChanged).isFalse();
    }
  }

  @Test
  void hasChangedShouldBeTrue() throws IOException {
    ModuleCalculator moduleCalculator = new ModuleCalculator();
    Path hashFile = Paths.get("target", "hasChanged", "src", "test", "resources", "hash.file");
    Files.deleteIfExists(hashFile);
    boolean hashChanged = moduleCalculator.hashChanged(Paths.get("src", "test", "resources"), hashFile, DEFAULT_EXCLUDES);
    assertThat(hashChanged).isTrue();
  }

  @Test
  void hasChangedFromTargetDirectoryIsTrue() throws IOException {
    ModuleCalculator moduleCalculator = new ModuleCalculator();
    Path hashFile = Paths.get("target", "hasXXXHasChanged", "src", "test", "resources", "hash.file");
    Files.deleteIfExists(hashFile);
    boolean x = moduleCalculator.hashChanged(Paths.get("src", "test", "resources"), hashFile, DEFAULT_EXCLUDES);
    assertThat(x).isTrue();
  }

  @Test
  void theSizeOfTheCalculatedHashIs32Bytes() throws IOException {
    ModuleCalculator moduleCalculator = new ModuleCalculator();
    ChecksumForFileResult x = moduleCalculator.calculateHashForDirectoryTree(Paths.get("."), DEFAULT_EXCLUDES);

    assertThat(x.getDigest().byteArray()).hasSize(32);
  }

  @Test
  void nullHashHasExpectedSize() throws IOException {
    Path hashFile = Paths.get("target", "hash.file");
    Files.write(hashFile, ChecksumForFileResult.NULL.getDigest().byteArray());
    assertThat(hashFile).hasSize(32);
  }

  @Test
  void readHashFromFile() throws IOException {
    Path hashFileLocation = Paths.get("src", "test", "resources", "target", "hash.file");
    ChecksumForFileResult checksumForFileResult = new ChecksumForFileResult(Files.readAllBytes(hashFileLocation));
    assertThat(checksumForFileResult.getDigest().byteArray()).containsExactly(
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x30, 0x31
    );
  }
}
