package com.jeremyworboys.expressionengine.ui.util;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.ColumnInfo;
import com.jeremyworboys.expressionengine.container.ContainerFile;
import org.jetbrains.annotations.Nullable;

public class ListTableUtil {

    public static class PathColumn extends ColumnInfo<ContainerFile, String> {

        private Project project;

        public PathColumn(Project project) {
            super("Path");
            this.project = project;
        }

        @Nullable
        @Override
        public String valueOf(ContainerFile containerFile) {
            return containerFile.getRelativePath(project);
        }
    }

}
