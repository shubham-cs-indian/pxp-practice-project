package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPrepareDataForLanguageInheritanceModel extends IModel {
  
  public static final String REFERENCED_LANGUAGES                  = "referencedLanguages";
  public static final String CONTENT_DIFF_FOR_LANGUAGE_INHERITANCE = "contentDiffForLanguageInheritance";
  
  public Map<String, ILanguageHierarchyModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, ILanguageHierarchyModel> referencedLanguages);
  
  public IKlassInstanceDiffForLanguageInheritanceModel getContentDiffForLanguageInheritance();
  
  public void setContentDiffForLanguageInheritance(
      IKlassInstanceDiffForLanguageInheritanceModel contentDiffForLanguageInheritance);
}
