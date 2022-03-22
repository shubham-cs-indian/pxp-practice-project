package com.cs.core.runtime.interactor.model.typeswitch;

import java.util.ArrayList;
import java.util.List;

public class AllowedTypesRequestModel implements IAllowedTypesRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          baseType;
  protected String          selectionType;
  protected String          id;
  protected String          userId;
  protected Long            from             = 0l;
  protected Long            size             = 0l;
  protected String          searchText       = "";
  protected String          searchColumn     = "";
  protected List<String>    idsToExclude;
  protected String          moduleId;
  protected List<String>    allowedEntities;
  
  @Override
  public String getSelectionType()
  {
    return selectionType;
  }
  
  @Override
  public void setSelectionType(String selectionType)
  {
    this.selectionType = selectionType;
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
  public List<String> getAllowedEntities()
  {
    if(allowedEntities == null) {
      allowedEntities = new ArrayList<>();
    }
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(List<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
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
