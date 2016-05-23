package com.jeremyworboys.expressionengine;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

public class ExpressionEngineFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(
            ExpressionEngineFileType.INSTANCE,
            StringUtil.join(ExpressionEngineFileType.INSTANCE.getExtensions(), FileTypeConsumer.EXTENSION_DELIMITER));
    }
}
