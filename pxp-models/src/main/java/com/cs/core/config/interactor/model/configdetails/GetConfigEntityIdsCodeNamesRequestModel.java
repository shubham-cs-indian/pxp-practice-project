package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class GetConfigEntityIdsCodeNamesRequestModel
    implements IGetConfigEntityIdsCodeNamesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          entityType;
  protected String          mapType;
  protected String          filter;
  protected List<String>    list;
  
  @Override
  public String getEntityType()
  {
    return this.entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
  
  @Override
  public String getMapType()
  {
    return mapType;
  }
  
  @Override
  public void setMapType(String mapType)
  {
    this.mapType = mapType;
  }
  
  @Override
  public String getFilter()
  {
    return filter;
  }
  
  @Override
  public void setFilter(String filter)
  {
    this.filter = filter;
  }
  
  @Override
  public List<String> getList()
  {
    if (list == null) {
      list = new ArrayList<String>();
    }
    return list;
  }
  
  @Override
  public void setList(List<String> list)
  {
    this.list = list;
  }
}
