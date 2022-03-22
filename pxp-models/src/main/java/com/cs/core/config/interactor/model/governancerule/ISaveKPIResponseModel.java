package com.cs.core.config.interactor.model.governancerule;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveKPIResponseModel extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String SAVE_KPI_DIFF     = "saveKPIDiff";
  public static final String STRATEGY_RESPONSE = "strategyResponse";
  
  public ISaveKPIDiffModel getSaveKPIDiff();
  
  public void setSaveKPIDiff(ISaveKPIDiffModel saveKPIDiff);
  
  public IGetKeyPerformanceIndexModel getStrategyResponse();
  
  public void setStrategyResponse(IGetKeyPerformanceIndexModel strategyResponse);
}
