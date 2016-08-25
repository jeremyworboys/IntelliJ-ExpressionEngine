package com.jeremyworboys.expressionengine;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.fileTypes.FileNameMatcherFactory;

public class ExpressionEngineFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(
            ExpressionEngineFileType.INSTANCE,
            FileNameMatcherFactory.getInstance().createMatcher("*." + ExpressionEngineFileType.INSTANCE.getDefaultExtension()));
    }
}
