package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetOrCreateLanguageModel extends IModel {
  
  public static final String LANGUAGES = "languages";
  
  public List<ILanguageModel> getLanguages();
  
  public void setLanguages(List<ILanguageModel> languages);
}
