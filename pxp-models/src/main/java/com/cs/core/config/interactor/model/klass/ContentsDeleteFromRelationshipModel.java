package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

public class ContentsDeleteFromRelationshipModel implements IContentsDeleteFromRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceContentId;
  protected String          relationshipId;
  protected List<String>    propertyIds;
  
  @Override
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
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
  
  @Override
  public List<String> getPropertyIds()
  {
    if (propertyIds == null) {
      propertyIds = new ArrayList<>();
    }
    return propertyIds;
  }
  
  @Override
  public void setPropertyIds(List<String> propertyIds)
  {
    this.propertyIds = propertyIds;
  }
}
