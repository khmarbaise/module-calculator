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
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.byteArray().length; i++) {
      sb.append(String.format("%02x", this.byteArray()[i]));
    }
    return "ByteArrayWrapper{" +
           "byteArray=" + sb +
           '}';
  }

  public byte[] byteArray() {
    return byteArray;
  }

}
