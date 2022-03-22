package com.cs.core.runtime.interactor.model.dataintegration;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcknowledgementBodyModel implements IAcknowledgementBodyModel {
  
  private static final long              serialVersionUID = 1L;
  private String                         status;
  private IAcknowledgementErrorBodyModel error;
  
  @Override
  public String getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  @Override
  public IAcknowledgementErrorBodyModel getError()
  {
    return error;
  }
  
  @Override
  public void setError(IAcknowledgementErrorBodyModel error)
  {
    this.error = error;
  }
}
