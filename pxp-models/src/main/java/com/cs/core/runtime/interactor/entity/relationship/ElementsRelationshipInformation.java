package com.cs.core.runtime.interactor.entity.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ElementsRelationshipInformation implements IElementsRelationshipInformation {
  
  private static final long serialVersionUID = 1L;
  
  protected String          commonRelationshipInstanceId;
  protected String          relationshipObjectId;
  
  @Override
  public String getCommonRelationshipInstanceId()
  {
    return commonRelationshipInstanceId;
  }
  
  @Override
  public void setCommonRelationshipInstanceId(String commonRelationshipInstanceId)
  {
    this.commonRelationshipInstanceId = commonRelationshipInstanceId;
  }
  
  @Override
  public String getRelationshipObjectId()
  {
    return relationshipObjectId;
  }
  
  @Override
  public void setRelationshipObjectId(String relationshipObjectId)
  {
    this.relationshipObjectId = relationshipObjectId;
  }
  
  @Override
  @JsonIgnore
  public String getId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setId(String id)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
}
