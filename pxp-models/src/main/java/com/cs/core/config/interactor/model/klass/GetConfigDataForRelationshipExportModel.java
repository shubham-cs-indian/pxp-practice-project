package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataRequestModel;

public class GetConfigDataForRelationshipExportModel extends GetConfigDataRequestModel implements IGetConfigDataForRelationshipExportModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          klassId;
  protected Long            from;
  protected Long            size;
  protected String          sortBy;
  protected String          sortOrder;
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }

  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
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
}
