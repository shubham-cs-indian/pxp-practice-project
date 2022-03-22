package com.cs.dam.config.interactor.model.downloadtracker;

public class GridViewTextFilterModel extends GridViewFilterModel implements IGridViewTextFilterModel {
  
  private static final long serialVersionUID = 1L;
  protected String          filterValues;
  
  public String getFilterValues()
  {
    return filterValues;
  }

  public void setFilterValues(String filterValues)
  {
    this.filterValues = filterValues;
  }
  
}
