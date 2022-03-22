package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetReferencedRelationshipPropertiesModel extends IModel {
  
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  
  // key:relationshipId
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
}
