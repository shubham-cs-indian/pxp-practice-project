package com.cs.dam.config.interactor.model.downloadtracker;

public interface IGridViewTextFilterModel extends IGridViewFilterModel {
  
  public static final String FILTER_VALUES = "filterValues";
  
  public String getFilterValues();
  public void setFilterValues(String filterValues);
}
