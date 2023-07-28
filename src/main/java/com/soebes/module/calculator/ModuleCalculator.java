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

import static com.soebes.module.calculator.FileSelector.selectAllFiles;
import static com.soebes.module.calculator.FileSelector.toChecksumForFile;

/**
 * @author Karl Heinz Marbaise
 */
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
