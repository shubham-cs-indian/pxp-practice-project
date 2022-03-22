package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetConfigDetailsToPrepareDataForLanguageInheritanceModel extends IModel {
  
  public static final String REFERENCED_LANGUAGES = "referencedLanguages";
  
  public Map<String, ILanguageHierarchyModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, ILanguageHierarchyModel> referencedLanguages);
}
