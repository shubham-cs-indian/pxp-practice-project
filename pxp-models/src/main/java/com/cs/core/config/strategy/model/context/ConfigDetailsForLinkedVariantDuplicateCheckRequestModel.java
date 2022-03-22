package com.cs.core.config.strategy.model.context;

public class ConfigDetailsForLinkedVariantDuplicateCheckRequestModel
    implements IConfigDetailsForLinkedVariantDuplicateCheckRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          relationshipId;
  protected String          contextId;
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
}
