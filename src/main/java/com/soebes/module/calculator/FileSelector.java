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

  static List<Path> selectAllFiles(Path start, List<String> excludes) throws IOException {
    try (Stream<Path> pathStream = Files.walk(start)) {
      return pathStream
          .filter(IS_VALID_FILE)
          .filter(file -> excludes.stream().noneMatch(exclude -> start.relativize(file).startsWith(exclude)))
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
