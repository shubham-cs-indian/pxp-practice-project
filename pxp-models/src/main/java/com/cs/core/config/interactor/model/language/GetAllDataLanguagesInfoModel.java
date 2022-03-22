package com.cs.core.config.interactor.model.language;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAllDataLanguagesInfoModel implements IGetAllDataLanguagesInfoModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected Map<String, IGetLanguagesInfoModel> referencedLanguages;
  
  @Override
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages()
  {
    if (referencedLanguages == null)
      referencedLanguages = new HashMap<>();
    return referencedLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
  
}
