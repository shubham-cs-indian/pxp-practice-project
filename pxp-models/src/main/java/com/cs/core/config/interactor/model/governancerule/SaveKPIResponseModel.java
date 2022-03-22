package com.cs.core.config.interactor.model.governancerule;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveKPIResponseModel extends ConfigResponseWithAuditLogModel
    implements ISaveKPIResponseModel {
  
  private static final long              serialVersionUID = 1L;
  protected ISaveKPIDiffModel            saveKPIDiff;
  protected IGetKeyPerformanceIndexModel response;
  
  @Override
  public ISaveKPIDiffModel getSaveKPIDiff()
  {
    return saveKPIDiff;
  }
  
  @Override
  @JsonDeserialize(as = SaveKPIDiffModel.class)
  public void setSaveKPIDiff(ISaveKPIDiffModel saveKPIDiff)
  {
    this.saveKPIDiff = saveKPIDiff;
  }
  
  @Override
  public IGetKeyPerformanceIndexModel getStrategyResponse()
  {
    return response;
  }
  
  @Override
  @JsonDeserialize(as = GetKeyPerformanceIndexModel.class)
  public void setStrategyResponse(IGetKeyPerformanceIndexModel response)
  {
    this.response = response;
  }
}
