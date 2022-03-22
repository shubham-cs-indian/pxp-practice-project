package com.cs.core.config.interactor.model.grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.constants.CommonConstants;

public class GetKlassesListRequestModel implements IGetKlassesListRequestModel {
  
  private static final long           serialVersionUID = 1L;
  
  protected Long                      from;
  protected Long                      size;
  protected String                    sortBy           = CommonConstants.LABEL_PROPERTY;
  protected String                    searchText;
  protected String                    sortOrder;
  protected String                    searchColumn;
  protected String                    baseType;
  protected Map<String, List<String>> typesToFilter;
  
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
  public Map<String, List<String>> getTypesToFilter()
  {
    if (typesToFilter == null) {
      typesToFilter = new HashMap<>();
    }
    return typesToFilter;
  }
  
  @Override
  public void setTypesToFilter(Map<String, List<String>> typesToFilter)
  {
    this.typesToFilter = typesToFilter;
  }
}
