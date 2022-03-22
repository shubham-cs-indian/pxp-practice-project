package com.cs.core.config.interactor.model.language;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetConfigDetailsToPrepareDataForLanguageInheritanceModel
    implements IGetConfigDetailsToPrepareDataForLanguageInheritanceModel {
  
  private static final long                      serialVersionUID = 1L;
  protected Map<String, ILanguageHierarchyModel> languageHierarchyModel;
  
  @Override
  public Map<String, ILanguageHierarchyModel> getReferencedLanguages()
  {
    return languageHierarchyModel;
  }
  
  @JsonDeserialize(contentAs = LanguageHierarchyModel.class)
  @Override
  public void setReferencedLanguages(Map<String, ILanguageHierarchyModel> referencedLanguages)
  {
    this.languageHierarchyModel = referencedLanguages;
  }
}
