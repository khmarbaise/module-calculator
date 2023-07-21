package com.soebes.module.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.soebes.module.calculator.FileSelector.selectAllFiles;
import static com.soebes.module.calculator.FileSelector.toChecksumForFile;

public final class ModuleCalculator {

  public ModuleCalculator() {
  }

  public boolean hashChanged(Path path, Path hashFile) {
    try {
      ChecksumForFileResult hashFromFile = ChecksumForFileResult.NON_EXISTENT;
      if (Files.exists(hashFile) && Files.isRegularFile(hashFile) && Files.isReadable(hashFile)) {
        hashFromFile = new ChecksumForFileResult(Files.readAllBytes(hashFile));
      }

      ChecksumForFileResult calculatedHash = calculateHashForDirectoryTree(path);

      //TODO: Create file only if not existing yet!
      Files.createDirectories(hashFile.getParent());
      Files.write(hashFile, calculatedHash.getDigest().byteArray());

      return !calculatedHash.equals(hashFromFile);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * TODO: Find a way to handle those exclusions; also get things from .gitignore?
   *    var defaultExcludes = List.of("target", ".git", ".github", ".idea");
   */
  public ChecksumForFileResult calculateHashForDirectoryTree(Path path) throws IOException {
    List<Path> paths = selectAllFiles(path);
    return paths
        .parallelStream()
        .map(toChecksumForFile)
        .reduce(ChecksumForFileResult.NULL, ChecksumForFileResult::accept);
  }
}
