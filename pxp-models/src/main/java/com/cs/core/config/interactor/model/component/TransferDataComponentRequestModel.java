package com.cs.core.config.interactor.model.component;

public class TransferDataComponentRequestModel implements ITransferDataComponentRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          eventInstanceId;
  
  @Override
  public String getEventInstanceId()
  {
    return eventInstanceId;
  }
  
  @Override
  public void setEventInstanceId(String eventInstanceId)
  {
    this.eventInstanceId = eventInstanceId;
  }
}
