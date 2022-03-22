package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.Map;

public interface IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel extends IModel {
  
  String REFERENCED_NATURE_RELATIONSHIPS = "referencedNatureRelationships";
  String REFERENCED_RELATIONSHIP         = "referencedRelationship";
  
  public Map<String, IReferencedNatureRelationshipInheritanceModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipInheritanceModel> getReferencedRelationship();
  
  public void setReferencedRelationship(
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship);
}
