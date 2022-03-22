package com.cs.dam.config.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GridViewTimeRangeFilterModel extends GridViewFilterModel
    implements IGridViewTimeRangeFilterModel {
  
  private static final long serialVersionUID = 1L;
  protected ITimeRange      filterValues     = new TimeRange();
  
  public ITimeRange getFilterValues()
  {
    return filterValues;
  }

  @JsonDeserialize(as = TimeRange.class)
  public void setFilterValues(ITimeRange filterValues)
  {
    this.filterValues = filterValues;
  }
  
}
