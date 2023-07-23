package com.soebes.module.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class FileSelector {
  private static final Predicate<Path> IS_REGULAR_FILE = Files::isRegularFile;
  private static final Predicate<Path> IS_READABLE = Files::isReadable;
  private static final Predicate<Path> IS_VALID_FILE = IS_REGULAR_FILE.and(IS_READABLE);

  private FileSelector() {
    // intentionally empty.
  }

  /**
   * TODO: Need to find a good way to define the exclusions via parameter?
   */
  static List<Path> selectAllFiles(Path start) throws IOException {
    try (Stream<Path> pathStream = Files.walk(start)) {
      return pathStream
          .filter(IS_VALID_FILE)
          .filter(s -> !start.relativize(s).startsWith("target"))
          .filter(s -> !start.relativize(s).startsWith(".git"))
          .filter(s -> !start.relativize(s).startsWith(".github"))
          .filter(s -> !start.relativize(s).startsWith(".idea"))
          .collect(Collectors.toList());
    }
  }

  static Function<Path, ChecksumForFileResult> toChecksumForFile = path -> {
    try {
      ChecksumResult checksumResult = new CalculateChecksum().forFile(path);
      return new ChecksumForFileResult(checksumResult.digest());
    } catch (IOException | NoSuchAlgorithmException e) {
      //Translate to RuntimeException.
      throw new RuntimeException(e.getClass().getName(), e);
    }
  };


}
