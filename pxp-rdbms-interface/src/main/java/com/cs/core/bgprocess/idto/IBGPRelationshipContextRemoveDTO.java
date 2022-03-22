package com.cs.core.bgprocess.idto;

public interface IBGPRelationshipContextRemoveDTO extends IInitializeBGProcessDTO {
  
  public String getRemovedSide1ContextId();
  
  public void setRemovedSide1ContextId(String removedSide1ContextId);
  
  public String getRemovedSide2ContextId();
  
  public void setRemovedSide2ContextId(String removedSide2ContextId);
  
  public Long getRelationshipPropertyId();
  
  public void setRelationshipPropertyId(Long relationshipPropertyId);
  
}
