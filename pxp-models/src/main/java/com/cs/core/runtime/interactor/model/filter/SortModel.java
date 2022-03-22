package com.cs.core.runtime.interactor.model.filter;

public class SortModel implements ISortModel {
  
  protected String  sortField;
  protected String  sortOrder;
  protected Boolean isNumeric;
  
  
  public SortModel() {
    
  }
  public SortModel(String sortField, String sortOrder, Boolean isNumeric)
  {
    super();
    this.sortField = sortField;
    this.sortOrder = sortOrder;
    this.isNumeric = isNumeric;
  }
  
  @Override
  public String getSortField()
  {
    return sortField;
  }
  
  @Override
  public void setSortField(String sortField)
  {
    this.sortField = sortField;
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
  public Boolean getIsNumeric()
  {
    return isNumeric;
  }
  
  @Override
  public void setIsNumeric(Boolean isNumeric)
  {
    this.isNumeric = isNumeric;
  }
}
