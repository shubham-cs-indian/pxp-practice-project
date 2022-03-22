package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetRolesOrUsersDataByPartnerIdModel implements IGetRolesOrUsersDataByPartnerIdModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected String                           searchColumn     = "";
  protected String                           searchText       = "";
  protected IGetConfigDataEntityRequestModel entities;
  protected String                           moduleId;
  protected String                           organizationId;
  
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
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
}
