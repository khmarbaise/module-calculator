package com.soebes.module.calculator;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FileSelectorTest {

  @Test
  void excludesNothing() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build()) {
      Path root = fileSystem.getPath("/");
      Files.createDirectories(root);
      Files.createDirectories(root.resolve("/root-project/.git"));
      Files.createDirectories(root.resolve("/root-project/.github"));
      Files.createDirectories(root.resolve("/root-project/target"));
      Files.createDirectories(root.resolve("/root-project/src/main/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/src/test/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/target/maven-status"));

      Files.write(root.resolve("/root-project/.git/example-in-git"),"example in .git directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/.github/example-in-github"),"example in .github directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/target/first.class"),"Example in target".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/src/main/java/com/example/FirstJava.java"),"Das ist ein Test".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/pom.xml"), "pom of the project.".getBytes(StandardCharsets.UTF_8));
      List<Path> result = FileSelector.selectAllFiles(root.resolve("/root-project"), Collections.emptyList());

      List<Path> expectedResult = Arrays.asList(
          root.resolve("/root-project/.git/example-in-git"),
          root.resolve("/root-project/.github/example-in-github"),
          root.resolve("/root-project/pom.xml"),
          root.resolve("/root-project/target/first.class"),
          root.resolve("/root-project/src/main/java/com/example/FirstJava.java")
      );
      assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }
  }

  @Test
  void excludesSingleTarget() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build()) {
      Path root = fileSystem.getPath("/");
      Files.createDirectories(root);
      Files.createDirectories(root.resolve("/root-project/.git"));
      Files.createDirectories(root.resolve("/root-project/.github"));
      Files.createDirectories(root.resolve("/root-project/target"));
      Files.createDirectories(root.resolve("/root-project/src/main/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/src/test/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/target/maven-status"));

      Files.write(root.resolve("/root-project/.git/example-in-git"),"example in .git directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/.github/example-in-github"),"example in .github directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/target/first.class"),"Example in target".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/src/main/java/com/example/FirstJava.java"),"Das ist ein Test".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/pom.xml"), "pom of the project.".getBytes(StandardCharsets.UTF_8));

      List<Path> result = FileSelector.selectAllFiles(root.resolve("/root-project"), Collections.singletonList("target"));

      List<Path> expectedResult = Arrays.asList(
          root.resolve("/root-project/.git/example-in-git"),
          root.resolve("/root-project/.github/example-in-github"),
          root.resolve("/root-project/pom.xml"),
          root.resolve("/root-project/src/main/java/com/example/FirstJava.java")
      );
      assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }
  }

  @Test
  void excludesTargetAndGit() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build()) {
      Path root = fileSystem.getPath("/");
      Files.createDirectories(root);
      Files.createDirectories(root.resolve("/root-project/.git"));
      Files.createDirectories(root.resolve("/root-project/.github"));
      Files.createDirectories(root.resolve("/root-project/target"));
      Files.createDirectories(root.resolve("/root-project/src/main/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/src/test/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/target/maven-status"));

      Files.write(root.resolve("/root-project/.git/example-in-git"),"example in .git directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/.github/example-in-github"),"example in .github directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/target/first.class"),"Example in target".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/src/main/java/com/example/FirstJava.java"),"Das ist ein Test".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/pom.xml"), "pom of the project.".getBytes(StandardCharsets.UTF_8));

      List<Path> result = FileSelector.selectAllFiles(root.resolve("/root-project"), Arrays.asList("target", ".git"));

      List<Path> expectedResult = Arrays.asList(
          root.resolve("/root-project/.github/example-in-github"),
          root.resolve("/root-project/pom.xml"),
          root.resolve("/root-project/src/main/java/com/example/FirstJava.java")
      );
      assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }
  }

  @Test
  void excludesTargetGitGitHub() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build()) {
      Path root = fileSystem.getPath("/");
      Files.createDirectories(root);
      Files.createDirectories(root.resolve("/root-project/.git"));
      Files.createDirectories(root.resolve("/root-project/.github"));
      Files.createDirectories(root.resolve("/root-project/target"));
      Files.createDirectories(root.resolve("/root-project/src/main/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/src/test/java/com/example"));
      Files.createDirectories(root.resolve("/root-project/target/maven-status"));

      Files.write(root.resolve("/root-project/.git/example-in-git"),"example in .git directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/.github/example-in-github"),"example in .github directory".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/target/first.class"),"Example in target".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/src/main/java/com/example/FirstJava.java"),"Das ist ein Test".getBytes(StandardCharsets.UTF_8));
      Files.write(root.resolve("/root-project/pom.xml"), "pom of the project.".getBytes(StandardCharsets.UTF_8));

      List<Path> result = FileSelector.selectAllFiles(root.resolve("/root-project"), Arrays.asList("target", ".git", ".github"));

      List<Path> expectedResult = Arrays.asList(
          root.resolve("/root-project/pom.xml"),
          root.resolve("/root-project/src/main/java/com/example/FirstJava.java")
      );
      assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
    }
  }


}