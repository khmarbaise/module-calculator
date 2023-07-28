package com.soebes.module.calculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
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

  public boolean hashChanged(Path path, Path hashFile, List<String> excludes) {
    LOGGER.debug("Starting: {} hashFile: {}", path, hashFile);
    try {
      ChecksumForFileResult hashFromFile = ChecksumForFileResult.NON_EXISTENT;
      if (Files.exists(hashFile) && Files.isRegularFile(hashFile) && Files.isReadable(hashFile)) {
        hashFromFile = new ChecksumForFileResult(Files.readAllBytes(hashFile));
      }

      ChecksumForFileResult calculatedHash = calculateHashForDirectoryTree(path,excludes);

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

  public ChecksumForFileResult calculateHashForDirectoryTree(Path path, List<String> excludes) throws IOException {
    LOGGER.debug("Starting: {}", path);
    List<Path> paths = selectAllFiles(path, excludes);
    return paths
        .parallelStream()
        .peek(f -> LOGGER.debug("File:{}", f))
        .map(toChecksumForFile)
        .peek(s -> LOGGER.debug("{}", s))
        .reduce(ChecksumForFileResult.NULL, ChecksumForFileResult::accept);
  }
}
