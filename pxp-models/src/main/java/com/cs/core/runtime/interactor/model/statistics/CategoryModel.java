package com.cs.core.runtime.interactor.model.statistics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class CategoryModel extends PathModel implements ICategoryModel {
  
  private static final long       serialVersionUID = 1L;
  
  protected List<IDimensionModel> dimensions;
  protected Long                  totalCount;
  
  @Override
  public List<IDimensionModel> getDimensions()
  {
    return dimensions;
  }
  
  @JsonDeserialize(contentAs = DimensionModel.class)
  @Override
  public void setDimensions(List<IDimensionModel> dimensions)
  {
    this.dimensions = dimensions;
  }
  
  @Override
  public Long getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Long totalCount)
  {
    this.totalCount = totalCount;
  }
}
