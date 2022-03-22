package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataRequestModel extends IModel {
  
  public static final String SEARCH_COLUMN = "searchColumn";
  public static final String SEARCH_TEXT   = "searchText";
  public static final String ENTITIES      = "entities";
  public static final String MODULE_ID     = "moduleId";
  
  public String getSearchColumn();
  
  public void setSearchColumn(String searchColumn);
  
  public String getSearchText();
  
  public void setSearchText(String searchText);
  
  public IGetConfigDataEntityRequestModel getEntities();
  
  public void setEntities(IGetConfigDataEntityRequestModel entities);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
}
