package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDefaultAndSupportingLanguagesModel extends IModel {
  
  public static final String DEFAULT_LANGUAGE    = "defaultLanguage";
  public static final String SUPPORTED_LANGUAGES = "supportedLanguages";
  
  public String getDefaultLanguage();
  
  public void setDefaultLanguage(String defaultLanguage);
  
  public List<IConfigEntityInformationModel> getSupportedLanguage();
  
  public void setSupportedLanguage(List<IConfigEntityInformationModel> supportedLanguages);
}
