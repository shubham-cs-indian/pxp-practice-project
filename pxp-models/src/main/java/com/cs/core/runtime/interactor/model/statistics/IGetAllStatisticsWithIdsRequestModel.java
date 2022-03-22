package com.cs.core.runtime.interactor.model.statistics;

import java.util.List;

public interface IGetAllStatisticsWithIdsRequestModel extends IGetAllStatisticsRequestModel {
  
  public static final String KPI_BLOCK_IDS             = "kpiBlockIds";
  
  public List<String> getKpiBlockIds();
  public void setKpiBlockIds(List<String> kpiBlockIds);
}
