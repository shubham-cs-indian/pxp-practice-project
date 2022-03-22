package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAcknowledgementBodyModel extends IModel {
  
  public static final String STATUS = "status";
  public static final String CORRELATION_ID = "correlationId";
  
  public String getStatus();
  
  public void setStatus(String status);
  
  public IAcknowledgementErrorBodyModel getError();
  
  public void setError(IAcknowledgementErrorBodyModel error);
}
