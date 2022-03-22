package com.cs.core.runtime.interactor.model.relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetReferencedRelationshipPropertiesModel
    implements IGetReferencedRelationshipPropertiesModel {
  
  private static final long                                     serialVersionUID = 1L;
  
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  
  @Override
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  @Override
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
}
