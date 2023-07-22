package com.soebes.module.calculator;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class ByteArrayWrapperTest {

  @Test
  void verify_hashcode_and_equals() {
    EqualsVerifier.forClass(ByteArrayWrapper.class).suppress(Warning.NULL_FIELDS).verify();
  }

  static final ByteArrayWrapper NULL = new ByteArrayWrapper(new byte[]{
      0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18,
      0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28,
      0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
  });

  @Test
  void name() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < NULL.byteArray().length; i++) {
      sb.append(String.format("%02x", NULL.byteArray()[i]));
    }
    System.out.println("sb = " + sb);
  }
}