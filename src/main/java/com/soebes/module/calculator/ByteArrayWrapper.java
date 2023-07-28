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

import java.util.Arrays;

/**
 * @author Karl Heinz Marbaise
 */
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
