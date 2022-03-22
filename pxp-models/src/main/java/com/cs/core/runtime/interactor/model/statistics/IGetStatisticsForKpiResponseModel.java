package com.cs.core.runtime.interactor.model.statistics;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetStatisticsForKpiResponseModel extends IModel{
    
  public static String KPI_BLOCKS = "kpiBlocks";
  public static String REFERENCED_KPI = "referencedKpi";
  
  public Map<String, String> getKpiBlocks();
  public void setKpiBlocks(Map<String, String> kpiBlocks);
  
  public Map<String , IIdLabelCodeMapModel> getReferencedKpi();
  public void setReferencedKpi(Map<String , IIdLabelCodeMapModel> referencedKpi);
  
}
