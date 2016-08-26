package com.jeremyworboys.expressionengine.stubs.indexes;

import com.intellij.util.indexing.FileContent;
import com.intellij.util.indexing.ID;
import com.jeremyworboys.expressionengine.container.ContainerHelper;
import com.jeremyworboys.expressionengine.container.ServiceSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ServicesDefinitionStubIndex extends SetupFileStubIndex<ServiceSerializable> {

    public static final ID<String, ServiceSerializable> KEY = ID.create("com.jeremyworboys.expressionengine.service_definition");

    @Override
    protected void buildIndex(FileContent inputData, Map<String, ServiceSerializable> index) {
        List<ServiceSerializable> servicesInFile = ContainerHelper.getServicesInFile(inputData.getPsiFile(), getServicePrefix(inputData));
        for (ServiceSerializable service : servicesInFile) {
            index.put(service.getId(), service);
        }
    }

    @NotNull
    @Override
    public ID<String, ServiceSerializable> getName() {
        return KEY;
    }

    @Override
    public int getVersion() {
        return 0;
    }
}
