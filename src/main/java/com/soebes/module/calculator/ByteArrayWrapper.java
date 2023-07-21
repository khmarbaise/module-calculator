package com.soebes.module.calculator;

import java.util.Arrays;

final class ByteArrayWrapper {
  private final byte[] byteArray;


  ByteArrayWrapper(byte[] byteArray) {
    this.byteArray = Arrays.copyOf(byteArray, byteArray.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ByteArrayWrapper)) return false;
    return Arrays.equals(byteArray, ((ByteArrayWrapper)o).byteArray);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(byteArray);
  }

  @Override
  public String toString() {
    return "ByteArrayWrapper{" +
           "byteArray=" + Arrays.toString(byteArray) +
           '}';
  }

  public byte[] byteArray() {
    return byteArray;
  }

}
