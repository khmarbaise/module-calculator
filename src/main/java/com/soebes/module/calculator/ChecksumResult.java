package com.soebes.module.calculator;

import java.util.Objects;

final class ChecksumResult {
  private final byte[] digest;

  ChecksumResult(byte[] digest) {
    this.digest = digest;
  }

  public byte[] digest() {
    return digest;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    ChecksumResult that = (ChecksumResult) obj;
    return Objects.equals(this.digest, that.digest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(digest);
  }

  @Override
  public String toString() {
    return "ChecksumResult[" +
           "digest=" + digest + ']';
  }


}
