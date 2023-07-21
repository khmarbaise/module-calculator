package com.soebes.module.calculator;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

final class ChecksumForFileResult {

  /**
   * This is the initial value.
   */
  static final ChecksumForFileResult NULL = new ChecksumForFileResult(new byte[]{
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
  });
  static final ChecksumForFileResult NON_EXISTENT = new ChecksumForFileResult(new byte[]{
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
      0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1,
  });

  private final ByteArrayWrapper digest;

  ChecksumForFileResult(ByteArrayWrapper digest) {
    this.digest = digest;
  }

  ChecksumForFileResult(byte[] digest) {
    this(new ByteArrayWrapper(digest));
  }

  ByteArrayWrapper getDigest() {
    return digest;
  }

  public static ChecksumForFileResult accept(ChecksumForFileResult first, ChecksumForFileResult second) {
    try {
      CalculateChecksum calculateChecksum = new CalculateChecksum();
      return new ChecksumForFileResult(calculateChecksum.forBytes(first.digest.byteArray(), second.digest.byteArray()).digest());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getClass().getName(), e);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    ChecksumForFileResult that = (ChecksumForFileResult) obj;
    return Objects.equals(this.digest, that.digest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(digest);
  }

}
