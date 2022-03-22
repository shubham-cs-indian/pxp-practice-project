package com.cs.dam.config.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "filterType",
visible = true)
public interface IGridViewFilterModel extends IModel {
  
  public static final String FILTER_TYPE        = "filterType";
  public static final String FILTER_FIELD       = "filterField";
  
  public String getFilterType();
  public void setFilterType(String filterType);
  
  public String getFilterField();
  public void setFilterField(String filterField);
}
