package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IUsageSummary extends IIdLabelCodeModel {
  
  public static final String TOTAL_COUNT = "totalCount";
  public static final String USED_BY     = "usedBy";
  
  public Integer getTotalCount();
  
  public void setTotalCount(Integer totalCount);
  
  public List<IUsedBy> getUsedBy();
  
  void setUsedBy(List<IUsedBy> usedBy);
}
