package com.cs.core.config.interactor.model.processdetails;

public interface IProcessSourceDestinationDetailsModel extends IProcessModel {
  
  public static final String SOURCE_ID      = "sourceId";
  public static final String DESTINATION_ID = "destinationId";
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
  
  public String getDestinationId();
  
  public void setDestinationId(String destinationId);
}
