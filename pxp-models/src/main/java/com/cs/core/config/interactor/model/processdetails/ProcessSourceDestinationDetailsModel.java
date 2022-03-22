package com.cs.core.config.interactor.model.processdetails;

public class ProcessSourceDestinationDetailsModel extends ProcessModel
    implements IProcessSourceDestinationDetailsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceId;
  protected String          destinationId;
  
  public String getSourceId()
  {
    return sourceId;
  }
  
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  public String getDestinationId()
  {
    return destinationId;
  }
  
  public void setDestinationId(String destinationId)
  {
    this.destinationId = destinationId;
  }
}
