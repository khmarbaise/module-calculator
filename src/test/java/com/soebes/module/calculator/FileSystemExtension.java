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

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.nio.file.FileSystem;

import static java.util.Objects.nonNull;

/**
 * @author Karl Heinz Marbaise
 */
class FileSystemExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  private static final String FILE_SYSTEM = "file_system";

  private static final ExtensionContext.Namespace FILE_SYSTEM_EXTENSION_NAMESPACE = ExtensionContext.Namespace.create(FileSystemExtension.class.getName());

  private ExtensionContext.Store getContext(ExtensionContext context) {
    return context.getStore(FILE_SYSTEM_EXTENSION_NAMESPACE);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    FileSystem fileSystem = MemoryFileSystemBuilder.newLinux().build(context.getUniqueId());
    getContext(context).put(FILE_SYSTEM + context.getUniqueId(), fileSystem);
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    FileSystem fileSystem = getContext(context).get(FILE_SYSTEM + context.getUniqueId(), FileSystem.class);
    if (nonNull(fileSystem)) {
      fileSystem.close();
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    if (!extensionContext.getElement().isPresent()) {
      return false;
    }

    return parameterContext.getParameter().getType() == FileSystem.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    if (parameterContext.getParameter().getType() == FileSystem.class) {
      return getContext(extensionContext).get(FILE_SYSTEM + extensionContext.getUniqueId(), FileSystem.class);
    }
    return null;
  }

}