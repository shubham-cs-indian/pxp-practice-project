package com.cs.core.runtime.interactor.model.goldenrecord;

public class GetRelationshipDataFromSourceRequestModel
    extends GetRelationshipDataFromSourcesRequestModel
    implements IGetRelationshipDataFromSourceRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceId;
  
  @Override
  public String getSourceId()
  {
    return sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
}
