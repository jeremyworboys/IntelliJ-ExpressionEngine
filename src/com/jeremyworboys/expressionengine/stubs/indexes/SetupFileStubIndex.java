package com.jeremyworboys.expressionengine.stubs.indexes;

import com.intellij.psi.PsiFile;
import com.intellij.util.indexing.DataIndexer;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileBasedIndexExtension;
import com.intellij.util.indexing.FileContent;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jeremyworboys.expressionengine.stubs.indexes.externalizer.ObjectStreamDataExternalizer;
import com.jetbrains.php.lang.PhpFileType;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

abstract public class SetupFileStubIndex<V extends Serializable> extends FileBasedIndexExtension<String, V> {

    protected final DataExternalizer<V> externalizer = new ObjectStreamDataExternalizer<>();
    protected final KeyDescriptor<String> keyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public DataIndexer<String, V, FileContent> getIndexer() {
        return inputData -> {
            Map<String, V> index = new THashMap<>();

            PsiFile psiFile = inputData.getPsiFile();
            if (!ExpressionEngineProjectComponent.isEnabledVersion3(psiFile.getProject()) || !isValidForIndex(inputData, psiFile)) {
                return index;
            }

            buildIndex(inputData, index);

            return index;
        };
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return keyDescriptor;
    }

    @NotNull
    @Override
    public DataExternalizer<V> getValueExternalizer() {
        return externalizer;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return file -> file.getFileType() == PhpFileType.INSTANCE;
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    protected abstract void buildIndex(FileContent inputData, Map<String, V> index);

    protected boolean isValidForIndex(FileContent inputData, PsiFile psiFile) {

        // Don't index non container setup files
        if (!psiFile.getName().endsWith(".setup.php")) {
            return false;
        }

        // Don't index files larger then files; use 5 MB here
        if (inputData.getFile().getLength() > getMaxFileByteSize()) {
            return false;
        }

        return true;
    }

    private int getMaxFileByteSize() {
        return 5242880;
    }
}
