package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigEntityIdsCodeNamesRequestModel extends IModel {
  
  public static final String ENTITY_TYPE = "entityType";
  public static final String MAP_TYPE    = "mapType";
  public static final String FILTER      = "filter";
  public static final String LIST        = "list";
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public String getMapType();
  
  public void setMapType(String mapType);
  
  public String getFilter();
  
  public void setFilter(String filter);
  
  public List<String> getList();
  
  public void setList(List<String> list);
}
