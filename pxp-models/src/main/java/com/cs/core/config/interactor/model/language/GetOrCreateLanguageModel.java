package com.cs.core.config.interactor.model.language;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetOrCreateLanguageModel implements IGetOrCreateLanguageModel {
  
  private static final long      serialVersionUID = 1L;
  protected List<ILanguageModel> languages;
  
  @Override
  public List<ILanguageModel> getLanguages()
  {
    return languages;
  }
  
  @Override
  @JsonDeserialize(contentAs = LanguageModel.class)
  public void setLanguages(List<ILanguageModel> languages)
  {
    this.languages = languages;
  }
}
