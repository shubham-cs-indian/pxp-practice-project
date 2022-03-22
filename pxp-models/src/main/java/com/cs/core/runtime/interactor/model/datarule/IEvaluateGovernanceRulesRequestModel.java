package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IEvaluateGovernanceRulesRequestModel extends IModel {
  
  public static final String KPI_LIST   = "kpiList";
  public static final String CONTENT_ID = "contentId";
  public static final String BASETYPE   = "baseType";
  
  public List<IGetKeyPerformanceIndexModel> getKpiList();
  
  public void setKpiList(List<IGetKeyPerformanceIndexModel> kpiList);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
