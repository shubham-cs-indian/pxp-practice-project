package com.cs.core.config.interactor.model.grid;

import java.util.List;

public class ConfigGetAllRequestModel implements IConfigGetAllRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            from;
  protected Long            size;
  protected String          sortBy           = "label";
  protected String          searchText;
  protected String          sortOrder;
  protected String          searchColumn;
  protected List<String>    types;
  protected Boolean         isStandard;
  protected String          userId;
  protected String          dashboardTabId;
  protected String          baseType;
  protected Boolean         isAbstract;
  protected List<String>    idsToExclude;
  protected String          workflowType;
  protected String          entityType;
  protected List<String>    ids;
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public String getSortBy()
  {
    return sortBy;
  }
  
  @Override
  public void setSortBy(String sortBy)
  {
    this.sortBy = sortBy;
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
  public String getSortOrder()
  {
    return sortOrder;
  }
  
  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
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
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getDashboardTabId()
  {
    return dashboardTabId;
  }
  
  @Override
  public void setDashboardTabId(String dashboardTabId)
  {
    this.dashboardTabId = dashboardTabId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Boolean getIsAbstract()
  {
    return this.isAbstract;
  }
  
  @Override
  public void setisAbstract(Boolean isAbstract)
  {
    this.isAbstract = isAbstract;
  }
  
  @Override
  public List<String> getIdsToExclude()
  {
    return idsToExclude;
  }
  
  @Override
  public void setIdsToExclude(List<String> idsToExclude)
  {
    this.idsToExclude = idsToExclude;
  }
  
  @Override
  public String getWorkflowType()
  {
    return workflowType;
  }
  
  @Override
  public void setWorkflowType(String workflowType)
  {
    this.workflowType = workflowType;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }

  @Override
  public List<String> getIds()
  {
    return ids;
  }

  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
}
