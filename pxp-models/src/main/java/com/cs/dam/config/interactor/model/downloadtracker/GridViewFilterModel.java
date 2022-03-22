package com.cs.dam.config.interactor.model.downloadtracker;

public class GridViewFilterModel implements IGridViewFilterModel {
  
  private static final long serialVersionUID = 1L;
  protected String          filterType       = this.getClass().getName();
  protected String          filterField;
  
  @Override
  public String getFilterType()
  {
    return filterType;
  }

  @Override
  public void setFilterType(String filterType)
  {
    this.filterType = filterType;
  }

  @Override
  public String getFilterField()
  {
    return filterField;
  }

  @Override
  public void setFilterField(String filterField)
  {
    this.filterField = filterField;
  }
}
