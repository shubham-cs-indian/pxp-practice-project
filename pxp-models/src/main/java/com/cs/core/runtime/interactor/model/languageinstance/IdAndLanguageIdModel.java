package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.config.interactor.model.configdetails.IIdAndLanguageIdModel;

public class IdAndLanguageIdModel implements IIdAndLanguageIdModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          languageId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLanguageId()
  {
    return languageId;
  }
  
  @Override
  public void setLanguageId(String languageId)
  {
    this.languageId = languageId;
  }
}
