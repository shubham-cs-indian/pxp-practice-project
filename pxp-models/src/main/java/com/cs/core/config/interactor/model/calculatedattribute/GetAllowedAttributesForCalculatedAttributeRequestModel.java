package com.cs.core.config.interactor.model.calculatedattribute;

import java.util.Map;

public class GetAllowedAttributesForCalculatedAttributeRequestModel
    implements IGetAllowedAttributesForCalculatedAttributeRequestModel {
  
  private static final long                               serialVersionUID = 1L;
  
  protected Long                                          from;
  protected Long                                          size;
  protected String                                        sortBy;
  protected String                                        sortOrder;
  protected String                                        searchColumn;
  protected String                                        searchText;
  protected String                                        calculatedAttributeType;
  protected String                                        calculatedAttributeUnit;
  protected Boolean                                       shouldAllowSelf  = false;;
  protected Map<String, Map<String, Map<String, String>>> mapping;
  protected String                                        attributeId;
  
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
  public String getCalculatedAttributeType()
  {
    return calculatedAttributeType;
  }
  
  @Override
  public void setCalculatedAttributeType(String calculatedAttributeType)
  {
    this.calculatedAttributeType = calculatedAttributeType;
  }
  
  @Override
  public String getCalculatedAttributeUnit()
  {
    return calculatedAttributeUnit;
  }
  
  @Override
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit)
  {
    this.calculatedAttributeUnit = calculatedAttributeUnit;
  }
  
  @Override
  public Boolean getShouldAllowSelf()
  {
    return shouldAllowSelf;
  }
  
  @Override
  public void getShouldAllowSelf(Boolean shouldAllowSelf)
  {
    this.shouldAllowSelf = shouldAllowSelf;
  }
  
  @Override
  public Map<String, Map<String, Map<String, String>>> getMapping()
  {
    return mapping;
  }
  
  @Override
  public void setMapping(Map<String, Map<String, Map<String, String>>> mapping)
  {
    this.mapping = mapping;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
}
