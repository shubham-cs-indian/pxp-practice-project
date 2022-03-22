package com.cs.core.runtime.interactor.model.statistics;

import java.util.ArrayList;
import java.util.List;

public class GetAllStatisticsWithIdsRequestModel extends GetAllStatisticsRequestModel
    implements IGetAllStatisticsWithIdsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>          kpiBlockIds;
  
  public List<String> getKpiBlockIds()
  {
    return kpiBlockIds;
  }
  
  @Override
  public void setKpiBlockIds(List<String> kpiBlockIds)
  {
    if(kpiBlockIds == null) {
      kpiBlockIds = new ArrayList<>();
    }
    this.kpiBlockIds = kpiBlockIds;
  }
}
