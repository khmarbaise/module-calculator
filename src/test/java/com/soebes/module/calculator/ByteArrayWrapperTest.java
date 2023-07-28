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

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Karl Heinz Marbaise
 */
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
  void checkExpectedToStringValue() {
    assertThat(NULL).hasToString("ByteArrayWrapper{byteArray=0102030405060708111213141516171821222324252627283132333435363738}");
  }
}