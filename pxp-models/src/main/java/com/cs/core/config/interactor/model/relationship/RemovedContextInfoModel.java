package com.cs.core.config.interactor.model.relationship;

public class RemovedContextInfoModel implements IRemovedContextInfoModel {
  
  private static final long serialVersionUID      = 1L;
  protected Long            relationshipPropertyId;
  protected String          removedSide1ContextId = "";
  protected String          removedSide2ContextId = "";
  
  @Override
  public Long getRelationshipPropertyId()
  {
    return relationshipPropertyId;
  }
  
  @Override
  public void setRelationshipPropertyId(Long relationshipPropertyId)
  {
    this.relationshipPropertyId = relationshipPropertyId;
  }
  
  @Override
  public String getRemovedSide1ContextId()
  {
    return removedSide1ContextId;
  }
  
  @Override
  public void setRemovedSide1ContextId(String removedSide1ContextId)
  {
    this.removedSide1ContextId = removedSide1ContextId;
  }
  
  @Override
  public String getRemovedSide2ContextId()
  {
    return removedSide2ContextId;
  }
  
  @Override
  public void setRemovedSide2ContextId(String removedSide2ContextId)
  {
    this.removedSide2ContextId = removedSide2ContextId;
  }
  
}
