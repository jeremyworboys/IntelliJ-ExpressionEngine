package com.jeremyworboys.expressionengine.stubs.indexes;

import com.intellij.util.indexing.FileContent;
import com.intellij.util.indexing.ID;
import com.jeremyworboys.expressionengine.container.ContainerHelper;
import com.jeremyworboys.expressionengine.container.model.ModelSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ModelsDefinitionStubIndex extends SetupFileStubIndex<ModelSerializable> {

    public static final ID<String, ModelSerializable> KEY = ID.create("com.jeremyworboys.expressionengine.model_definition");

    @Override
    protected void buildIndex(FileContent inputData, Map<String, ModelSerializable> index) {
        List<ModelSerializable> modelsInFile = ContainerHelper.getModelsInFile(inputData.getPsiFile());
        for (ModelSerializable model : modelsInFile) {
            index.put(model.getId(), model);
        }
    }

    @NotNull
    @Override
    public ID<String, ModelSerializable> getName() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
