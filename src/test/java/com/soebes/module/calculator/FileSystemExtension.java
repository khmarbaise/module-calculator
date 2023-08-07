package com.soebes.module.calculator;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.nio.file.FileSystem;

import static java.util.Objects.nonNull;

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
    Class<?> type = parameterContext.getParameter().getType();
    if (type == FileSystem.class) {
      return getContext(extensionContext).get(FILE_SYSTEM + extensionContext.getUniqueId(), FileSystem.class);
    }
    return null;
  }

}