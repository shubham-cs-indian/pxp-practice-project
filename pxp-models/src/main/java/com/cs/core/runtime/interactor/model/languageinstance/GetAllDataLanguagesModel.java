package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

import java.util.List;

public class GetAllDataLanguagesModel extends ConfigGetAllRequestModel
    implements IGetAllDataLanguagesModel {
  
  private static final long serialVersionUID = 1L;
  protected long            id;
  protected List<String>    languageCodes;
  
  @Override
  public List<String> getLanguageCodes()
  {
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public long getId()
  {
    return id;
  }
  
  @Override
  public void setId(long id)
  {
    this.id = id;
  }
}
