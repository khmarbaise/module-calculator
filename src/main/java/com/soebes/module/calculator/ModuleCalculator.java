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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.soebes.module.calculator.FileSelector.selectFiles;
import static com.soebes.module.calculator.FileSelector.toChecksumForFile;

/**
 * @author Karl Heinz Marbaise
 */
public final class ModuleCalculator {

  private final Logger LOGGER = LoggerFactory.getLogger( getClass() );

  public ModuleCalculator() {
    // intentionally empty.
  }

  /**
   * This will calculate the current hash over the directory tree given by
   * {@code path} and compares it with {@code hashFile}.
   * @param path The root directory where to start calculating the hash.
   * @param hashFile The hashFile which is read if existing and compared to the actual hash.
   * @param excludes The list of excluded directories.
   * @return {@code false} if there is no difference between hashFile and actual hash otherwise {@code true}.
   * If the {@code hashFile} does not exist it will be assumed there is a change and the result {@code true}.
   */
  public boolean hashChanged(Path path, Path hashFile, List<String> excludes) {
    LOGGER.debug("Starting: {} hashFile: {}", path, hashFile);
    try {
      ChecksumForFileResult hashFromFile = ChecksumForFileResult.NON_EXISTENT;
      if (Files.exists(hashFile) && Files.isRegularFile(hashFile) && Files.isReadable(hashFile)) {
        hashFromFile = new ChecksumForFileResult(Files.readAllBytes(hashFile));
      }

      ChecksumForFileResult calculatedHash = calculateHashForDirectoryTree(path,excludes);

      Files.createDirectories(hashFile.getParent());
      Files.write(hashFile, calculatedHash.getDigest().byteArray());

      boolean changed = !calculatedHash.equals(hashFromFile);
      LOGGER.debug("changed: {} hashFromFile: {} calculatedHash: {}", changed, hashFromFile.getDigest(), calculatedHash.getDigest());
      return changed;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  ChecksumForFileResult calculateHashForDirectoryTree(Path path, List<String> excludes) throws IOException {
    LOGGER.debug("Starting: {}", path);
    List<Path> paths = selectFiles(path, excludes);
    if (LOGGER.isDebugEnabled()) {
      paths.forEach(s -> LOGGER.debug(" -> {}", s));
    }
    return paths
        .parallelStream()
        .map(toChecksumForFile)
        .reduce(ChecksumForFileResult.NULL, ChecksumForFileResult::accept);
  }
}
