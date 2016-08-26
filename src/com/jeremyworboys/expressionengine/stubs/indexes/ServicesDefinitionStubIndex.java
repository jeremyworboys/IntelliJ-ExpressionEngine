package com.jeremyworboys.expressionengine.stubs.indexes;

import com.intellij.psi.PsiFile;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import com.jeremyworboys.expressionengine.ExpressionEngineProjectComponent;
import com.jeremyworboys.expressionengine.container.ContainerHelper;
import com.jeremyworboys.expressionengine.container.ServiceSerializable;
import com.jeremyworboys.expressionengine.stubs.indexes.externalizer.ObjectStreamDataExternalizer;
import com.jetbrains.php.lang.PhpFileType;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ServicesDefinitionStubIndex extends FileBasedIndexExtension<String, ServiceSerializable> {

    public static final ID<String, ServiceSerializable> KEY = ID.create("com.jeremyworboys.expressionengine.service_definition");

    private static int VERSION = 0;
    private static int MAX_FILE_BYTE_SIZE = 5242880;
    private static ObjectStreamDataExternalizer<ServiceSerializable> EXTERNALIZER = new ObjectStreamDataExternalizer<>();

    private final KeyDescriptor<String> keyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public DataIndexer<String, ServiceSerializable, FileContent> getIndexer() {
        return inputData -> {
            Map<String, ServiceSerializable> map = new THashMap<>();

            PsiFile psiFile = inputData.getPsiFile();
            if (!ExpressionEngineProjectComponent.isEnabledVersion3(psiFile.getProject()) || !isValidForIndex(inputData, psiFile)) {
                return map;
            }

            for (ServiceSerializable service : ContainerHelper.getServicesInFile(psiFile)) {
                map.put(service.getId(), service);
            }

            return map;
        };
    }

    private boolean isValidForIndex(FileContent inputData, PsiFile psiFile) {
        String fileName = psiFile.getName();

        // Don't index non container setup files
        if (!fileName.endsWith(".setup.php")) {
            return false;
        }

        // Don't index files larger then files; use 5 MB here
        if (inputData.getFile().getLength() > MAX_FILE_BYTE_SIZE) {
            return false;
        }

        return true;
    }

    @NotNull
    @Override
    public ID<String, ServiceSerializable> getName() {
        return KEY;
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return keyDescriptor;
    }

    @NotNull
    @Override
    public DataExternalizer<ServiceSerializable> getValueExternalizer() {
        return EXTERNALIZER;
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

    @Override
    public int getVersion() {
        return VERSION;
    }
}
