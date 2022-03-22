package com.cs.dam.config.interactor.model.downloadtracker;

import java.util.List;

public interface IGridViewListFilterModel extends IGridViewFilterModel {
  
  public static final String FILTER_VALUES = "filterValues";
  
  public List<String> getFilterValues();
  public void setFilterValues(List<String> filterValues);
}
