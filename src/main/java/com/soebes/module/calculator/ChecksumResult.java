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

import java.util.Objects;

/**
 * @author Karl Heinz Marbaise
 */
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
