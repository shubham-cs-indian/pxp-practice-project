package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.filter.ISortModel;

public interface IGetGoldenRecordRuleBucketInstancesRequestModel extends ISortModel {
  
  public static String FROM       = "from";
  public static String SIZE       = "size";
  public static String SORT_FIELD = "sortField";
  public static String SORT_ORDER = "sortOrder";
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
}
