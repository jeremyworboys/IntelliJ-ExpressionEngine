package com.jeremyworboys.expressionengine.template;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.fileTypes.FileNameMatcherFactory;

public class ExpressionEngineFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(
            ExpressionEngineFileType.INSTANCE,
            // TODO: See if this can be used to limit files to template path
            FileNameMatcherFactory.getInstance().createMatcher("*." + ExpressionEngineFileType.INSTANCE.getDefaultExtension()));
    }
}
