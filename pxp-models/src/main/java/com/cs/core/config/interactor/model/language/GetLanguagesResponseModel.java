package com.cs.core.config.interactor.model.language;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetLanguagesResponseModel implements IGetLanguagesResponseModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected String                       defaultLanguage;
  protected List<IGetLanguagesInfoModel> dataLanguages;
  protected List<IGetLanguagesInfoModel> uiLanguages;
  
  @Override
  public List<IGetLanguagesInfoModel> getDataLanguages()
  {
    if (dataLanguages == null) {
      dataLanguages = new ArrayList<>();
    }
    return dataLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  public void setDataLanguages(List<IGetLanguagesInfoModel> dataLanguages)
  {
    this.dataLanguages = dataLanguages;
  }
  
  @Override
  public List<IGetLanguagesInfoModel> getUserInterfaceLanguages()
  {
    if (uiLanguages == null) {
      uiLanguages = new ArrayList<>();
    }
    return uiLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  public void setUserInterfaceLanguages(List<IGetLanguagesInfoModel> uiLanguages)
  {
    this.uiLanguages = uiLanguages;
  }
  
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
}
