package com.cs.core.config.interactor.model.language;

public class GetLanguagesRequestModel implements IGetLanguagesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isGetDataLanguages;
  protected Boolean         isGetUILanguages;
  protected String          dataLanguage;
  protected String          uiLanguage;
  
  @Override
  public Boolean getIsGetDataLanguages()
  {
    return isGetDataLanguages;
  }
  
  @Override
  public void setIsGetDataLanguages(Boolean isGetDataLanguages)
  {
    this.isGetDataLanguages = isGetDataLanguages;
  }
  
  @Override
  public Boolean getIsGetUILanguages()
  {
    return isGetUILanguages;
  }
  
  @Override
  public void setIsGetUILanguages(Boolean isGetUILanguages)
  {
    this.isGetUILanguages = isGetUILanguages;
  }
  
  @Override
  public String getDataLanguage()
  {
    return dataLanguage;
  }
  
  @Override
  public void setDataLanguage(String dataLanguage)
  {
    this.dataLanguage = dataLanguage;
  }
  
  @Override
  public String getUiLanguage()
  {
    return uiLanguage;
  }
  
  @Override
  public void setUiLanguage(String uiLanguage)
  {
    this.uiLanguage = uiLanguage;
  }
}
