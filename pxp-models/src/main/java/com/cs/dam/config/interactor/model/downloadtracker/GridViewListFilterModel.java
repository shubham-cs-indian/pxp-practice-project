package com.cs.dam.config.interactor.model.downloadtracker;

import java.util.ArrayList;
import java.util.List;

public class GridViewListFilterModel extends GridViewFilterModel implements IGridViewListFilterModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    filterValues     = new ArrayList<>();
  
  public List<String> getFilterValues()
  {
    return filterValues;
  }

  public void setFilterValues(List<String> filterValues)
  {
    this.filterValues = filterValues;
  }
  
}
