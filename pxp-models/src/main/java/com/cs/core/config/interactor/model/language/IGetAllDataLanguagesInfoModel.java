package com.cs.core.config.interactor.model.language;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllDataLanguagesInfoModel extends IModel {
  
  public static final String REFERENCED_LANGUAGES = "referencedLanguages";
  
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages);
}
