package com.cs.core.runtime.interactor.model.statistics;

import java.util.List;

public interface ICategoryModel extends IPathModel {
  
  public static final String DIMENSIONS  = "dimensions";
  public static final String TOTAL_COUNT = "totalCount";
  
  public List<IDimensionModel> getDimensions();
  
  public void setDimensions(List<IDimensionModel> dimensions);
  
  public Long getTotalCount();
  
  public void setTotalCount(Long totalCount);
}
