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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Karl Heinz Marbaise
 */
final class FileSelector {
  private static final Predicate<Path> IS_REGULAR_FILE = Files::isRegularFile;
  private static final Predicate<Path> IS_READABLE = Files::isReadable;
  private static final Predicate<Path> IS_VALID_FILE = IS_REGULAR_FILE.and(IS_READABLE);

  private FileSelector() {
    // intentionally empty.
  }

  static List<Path> selectFiles(Path start, List<String> excludes) throws IOException {
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
