package com.soebes.module.calculator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class CalculateChecksum {
  private static final int BUFFER_SIZE = 64 * 1024;

  private final MessageDigest messageDigest;

  CalculateChecksum() throws NoSuchAlgorithmException {
    this.messageDigest = MessageDigest.getInstance("SHA-256");
  }

  ChecksumResult forFile(Path file) throws IOException {
    try (InputStream fis = Files.newInputStream(file)) {
      return forFile(fis);
    }
  }

  ChecksumResult forBytes(byte[] left, byte[] right) {
    messageDigest.update(left);
    messageDigest.update(right);
    return new ChecksumResult(messageDigest.digest());
  }

  private ChecksumResult forFile(InputStream inputStream) throws IOException {
    byte[] dataBytes = new byte[BUFFER_SIZE];

    int nread;
    while ((nread = inputStream.read(dataBytes)) != -1) {
      messageDigest.update(dataBytes, 0, nread);
    }
    return new ChecksumResult(messageDigest.digest());
  }

}
