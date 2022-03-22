package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

import java.util.List;

public interface IGetAllDataLanguagesModel extends IConfigGetAllRequestModel {
  
  public static final String ID             = "id";
  public static final String LANGUAGE_CODES = "languageCodes";
  
  public long getId();
  
  public void setId(long id);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
}
