package com.cs.core.runtime.interactor.model.goldenrecord;

public interface IGetRelationshipDataFromSourceRequestModel
    extends IGetRelationshipDataFromSourcesRequestModel {
  
  public static final String SOURCE_ID = "sourceId";
  
  public String getSourceId();
  
  public void setSourceId(String sourceId);
}
