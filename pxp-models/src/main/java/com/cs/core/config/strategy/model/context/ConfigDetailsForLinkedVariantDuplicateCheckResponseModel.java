package com.cs.core.config.strategy.model.context;

public class ConfigDetailsForLinkedVariantDuplicateCheckResponseModel
    implements IConfigDetailsForLinkedVariantDuplicateCheckResponseModel {
  
  private static final long serialVersionUID     = 1L;
  
  protected Boolean         isDuplicationAllowed = false;
  protected Long            relationshipIId;
  
  @Override
  public Boolean getIsDuplicationAllowed()
  {
    return isDuplicationAllowed;
  }
  
  @Override
  public void setIsDuplicationAllowed(Boolean isDuplicationAllowed)
  {
    this.isDuplicationAllowed = isDuplicationAllowed;
  }
  
  @Override
  public Long getRelationshipIId()
  {
    return relationshipIId;
  }
  
  @Override
  public void setRelationshipIId(Long relationshipIId)
  {
    this.relationshipIId = relationshipIId;
  }
  
}
