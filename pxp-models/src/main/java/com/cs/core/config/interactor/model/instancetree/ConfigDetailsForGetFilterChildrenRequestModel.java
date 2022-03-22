package com.cs.core.config.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.List;

public class ConfigDetailsForGetFilterChildrenRequestModel
    implements IConfigDetailsForGetFilterChildrenRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    allowedEntites;
  protected String          userId;
  protected String          id;
  protected String          filterType;
  protected String          kpiId;
  protected String          searchText;
  
  @Override
  public List<String> getAllowedEntities()
  {
    if (allowedEntites == null) {
      allowedEntites = new ArrayList<>();
    }
    return allowedEntites;
  }
  
  @Override
  public void setAllowedEntities(List<String> allowedEntites)
  {
    this.allowedEntites = allowedEntites;
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
  public String getFilterType()
  {
    return filterType;
  }
  
  @Override
  public void setFilterType(String type)
  {
    this.filterType = type;
  }
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
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
}
