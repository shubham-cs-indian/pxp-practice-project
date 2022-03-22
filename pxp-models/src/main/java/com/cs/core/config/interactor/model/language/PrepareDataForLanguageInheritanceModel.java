package com.cs.core.config.interactor.model.language;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class PrepareDataForLanguageInheritanceModel
    implements IPrepareDataForLanguageInheritanceModel {
  
  private static final long                               serialVersionUID = 1L;
  protected Map<String, ILanguageHierarchyModel>          languageHeirarchy;
  protected IKlassInstanceDiffForLanguageInheritanceModel contentInstanceDiffForLanguageInheritance;
  
  @Override
  public Map<String, ILanguageHierarchyModel> getReferencedLanguages()
  {
    return languageHeirarchy;
  }
  
  @JsonDeserialize(contentAs = LanguageHierarchyModel.class)
  @Override
  public void setReferencedLanguages(Map<String, ILanguageHierarchyModel> referencedLanguages)
  {
    this.languageHeirarchy = referencedLanguages;
  }
  
  @Override
  public IKlassInstanceDiffForLanguageInheritanceModel getContentDiffForLanguageInheritance()
  {
    return contentInstanceDiffForLanguageInheritance;
  }
  
  @JsonDeserialize(as = KlassInstanceDiffForLanguageInheritanceModel.class)
  @Override
  public void setContentDiffForLanguageInheritance(
      IKlassInstanceDiffForLanguageInheritanceModel contentDiffForLanguageInheritance)
  {
    this.contentInstanceDiffForLanguageInheritance = contentDiffForLanguageInheritance;
  }
}
