package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.config.interactor.model.governancerule.GetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class EvaluateGovernanceRulesRequestModel implements IEvaluateGovernanceRulesRequestModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected List<IGetKeyPerformanceIndexModel> kpiList;
  protected String                             contentId;
  protected String                             baseType;
  
  @Override
  public List<IGetKeyPerformanceIndexModel> getKpiList()
  {
    return kpiList;
  }
  
  @JsonDeserialize(contentAs = GetKeyPerformanceIndexModel.class)
  @Override
  public void setKpiList(List<IGetKeyPerformanceIndexModel> kpiList)
  {
    this.kpiList = kpiList;
  }
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
