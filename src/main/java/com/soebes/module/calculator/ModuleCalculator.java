package com.soebes.module.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.soebes.module.calculator.FileSelector.selectAllFiles;
import static com.soebes.module.calculator.FileSelector.toChecksumForFile;

public final class ModuleCalculator {

  private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

  public ModuleCalculator() {
    // intentionally empty.
  }

  public boolean hashChanged(Path path, Path hashFile) {
    LOGGER.debug("Starting: {} hashFile: {}", path, hashFile);
    try {
      ChecksumForFileResult hashFromFile = ChecksumForFileResult.NON_EXISTENT;
      if (Files.exists(hashFile) && Files.isRegularFile(hashFile) && Files.isReadable(hashFile)) {
        hashFromFile = new ChecksumForFileResult(Files.readAllBytes(hashFile));
      }

      ChecksumForFileResult calculatedHash = calculateHashForDirectoryTree(path);

      //TODO: Create file only if not existing yet!
      Files.createDirectories(hashFile.getParent());
      Files.write(hashFile, calculatedHash.getDigest().byteArray());

      boolean changed = !calculatedHash.equals(hashFromFile);
      LOGGER.debug("changed: {} hashFromFile: {} calculatedHash: {}", changed, hashFromFile.getDigest(), calculatedHash.getDigest());
      return changed;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * TODO: Find a way to handle those exclusions; also get things from .gitignore?
   *    var defaultExcludes = List.of("target", ".git", ".github", ".idea");
   */
  public ChecksumForFileResult calculateHashForDirectoryTree(Path path) throws IOException {
    LOGGER.debug("Starting: {}", path);
    List<Path> paths = selectAllFiles(path);
    return paths
        .parallelStream()
        .peek(f -> LOGGER.debug("File:{}", f))
        .map(toChecksumForFile)
        .peek(s -> LOGGER.debug("{}", s))
        .reduce(ChecksumForFileResult.NULL, ChecksumForFileResult::accept);
  }
}
