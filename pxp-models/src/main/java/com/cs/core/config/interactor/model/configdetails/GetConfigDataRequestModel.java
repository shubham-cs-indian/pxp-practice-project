package com.cs.core.config.interactor.model.configdetails;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataRequestModel implements IGetConfigDataRequestModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected String                           searchColumn     = "";
  protected String                           searchText       = "";
  protected IGetConfigDataEntityRequestModel entities;
  protected String                           moduleId;
  
  @Override
  public String getSearchColumn()
  {
    return searchColumn;
  }
  
  @Override
  public void setSearchColumn(String searchColumn)
  {
    this.searchColumn = searchColumn;
  }
  
  @Override
  public String getSearchText()
  {
    return searchText;
  }
  
  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
  }
  
  @Override
  public IGetConfigDataEntityRequestModel getEntities()
  {
    return entities;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityRequestModel.class)
  public void setEntities(IGetConfigDataEntityRequestModel entities)
  {
    this.entities = entities;
  }
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
}
