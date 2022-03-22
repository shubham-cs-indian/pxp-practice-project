package com.cs.core.config.interactor.model.language;

import java.util.ArrayList;
import java.util.List;

public class DeleteLanguageRequestModel implements IDeleteLanguageRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    ids;
  protected String          dataLanguage;
  protected String          uiLanguage;
  
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
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      return new ArrayList<>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
}
