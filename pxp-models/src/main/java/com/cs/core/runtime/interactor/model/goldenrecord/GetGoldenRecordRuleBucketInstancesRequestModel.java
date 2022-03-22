package com.cs.core.runtime.interactor.model.goldenrecord;

public class GetGoldenRecordRuleBucketInstancesRequestModel
    implements IGetGoldenRecordRuleBucketInstancesRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Integer         from;
  protected Integer         size;
  protected String          sortField;
  protected String          sortOrder;
  
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
    return null;
  }
  
  @Override
  public void setIsNumeric(Boolean isNumeric)
  {
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
}
