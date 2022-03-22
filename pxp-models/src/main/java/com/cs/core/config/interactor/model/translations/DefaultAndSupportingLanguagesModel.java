package com.cs.core.config.interactor.model.translations;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DefaultAndSupportingLanguagesModel implements IDefaultAndSupportingLanguagesModel {
  
  private static final long                     serialVersionUID = 1L;
  protected String                              defaultLanguage;
  protected List<IConfigEntityInformationModel> supportedLanguages;
  
  @Override
  public String getDefaultLanguage()
  {
    return defaultLanguage;
  }
  
  @Override
  public void setDefaultLanguage(String defaultLanguage)
  {
    this.defaultLanguage = defaultLanguage;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getSupportedLanguage()
  {
    if (supportedLanguages == null) {
      supportedLanguages = new ArrayList<>();
    }
    return supportedLanguages;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setSupportedLanguage(List<IConfigEntityInformationModel> supportedLanguages)
  {
    this.supportedLanguages = supportedLanguages;
  }
}
