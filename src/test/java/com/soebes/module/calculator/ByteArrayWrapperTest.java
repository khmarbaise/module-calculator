package com.soebes.module.calculator;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class ByteArrayWrapperTest {

  @Test
  void verify_hashcode_and_equals() {
    EqualsVerifier.forClass(ByteArrayWrapper.class).suppress(Warning.NULL_FIELDS).verify();
  }
}