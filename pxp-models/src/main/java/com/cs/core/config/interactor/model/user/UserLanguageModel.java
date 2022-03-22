package com.cs.core.config.interactor.model.user;

public class UserLanguageModel implements IUserLanguageModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isLanguageChanged = false;
  protected String          preferredDataLanguage;
  protected String          preferredUILanguage;
  
  @Override
  public String getPreferredDataLanguage()
  {
    return preferredDataLanguage;
  }
  
  @Override
  public void setPreferredDataLanguage(String preferredDataLanguage)
  {
    this.preferredDataLanguage = preferredDataLanguage;
  }
  
  @Override
  public String getPreferredUILanguage()
  {
    return preferredUILanguage;
  }
  
  @Override
  public void setPreferredUILanguage(String preferredUILanguage)
  {
    this.preferredUILanguage = preferredUILanguage;
  }

  @Override
  public Boolean getIsLanguageChanged()
  {
    return isLanguageChanged;
  }

  @Override
  public void setIsLanguageChanged(Boolean isLanguageChanged)
  {
    this.isLanguageChanged = isLanguageChanged;
  }
}
