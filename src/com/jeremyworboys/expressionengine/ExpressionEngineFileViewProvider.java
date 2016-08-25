package com.jeremyworboys.expressionengine;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.LanguageSubstitutors;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.jeremyworboys.expressionengine.psi.ExpressionEngineTypes;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

public class ExpressionEngineFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements TemplateLanguageFileViewProvider {
    private final Language templateDataLanguage;
    private static final TemplateDataElementType htmlTemplateDataType;

    public ExpressionEngineFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled) {
        super(manager, virtualFile, eventSystemEnabled);

        Language language = TemplateDataLanguageMappings.getInstance(manager.getProject()).getMapping(virtualFile);
        if (language == null) {
            language = StdFileTypes.HTML.getLanguage();
        }
        if (language instanceof TemplateLanguage) {
            this.templateDataLanguage = PlainTextLanguage.INSTANCE;
        } else {
            this.templateDataLanguage = LanguageSubstitutors.INSTANCE.substituteLanguage(language, virtualFile, manager.getProject());
        }
    }

    public ExpressionEngineFileViewProvider(PsiManager manager, VirtualFile virtualFile, boolean eventSystemEnabled, Language templateDataLanguage) {
        super(manager, virtualFile, eventSystemEnabled);
        this.templateDataLanguage = templateDataLanguage;
    }

    @NotNull
    @Override
    public Language getBaseLanguage() {
        return ExpressionEngineLanguage.INSTANCE;
    }

    @NotNull
    @Override
    public Language getTemplateDataLanguage() {
        return templateDataLanguage;
    }

    @NotNull
    @Override
    public Set<Language> getLanguages() {
        return new THashSet<>(Arrays.asList(new Language[]{ExpressionEngineLanguage.INSTANCE, this.getTemplateDataLanguage()}));
    }

    @Override
    protected MultiplePsiFilesPerDocumentFileViewProvider cloneInner(VirtualFile fileCopy) {
        return new ExpressionEngineFileViewProvider(getManager(), fileCopy, false, this.templateDataLanguage);
    }

    @Nullable
    @Override
    protected PsiFile createFile(@NotNull Language lang) {
        ParserDefinition parser = LanguageParserDefinitions.INSTANCE.forLanguage(lang);
        if (parser == null) {
            return null;
        } else {
            PsiFileImpl file;
            if (lang == this.getTemplateDataLanguage()) {
                file = (PsiFileImpl) parser.createFile(this);
                file.setContentElementType(htmlTemplateDataType);
                return file;
            } else if (lang == this.getBaseLanguage()) {
                return parser.createFile(this);
            } else {
                return null;
            }
        }
    }

    static {
        htmlTemplateDataType = new TemplateDataElementType(
            "HTML in ExpressionEngine",
            ExpressionEngineLanguage.INSTANCE,
            ExpressionEngineTypes.T_CONTENT,
            ExpressionEngineTypes.OUTER_CONTENT) {
        };
    }
}
