package com.cs.dam.config.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;

public interface IGridViewTimeRangeFilterModel extends IGridViewFilterModel {
  
  public static final String FILTER_VALUES = "filterValues";
  
  public ITimeRange getFilterValues();
  public void setFilterValues(ITimeRange filterValues);
}
