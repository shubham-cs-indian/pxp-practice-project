package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.Map;

public interface IGetNatureRelationshipInfoForRollbackResponseModel extends IModel {
  
  String PARENT_REFERENCED_NATURE_RELATIONSHIPS = "parentReferencedNatureRelationships";
  String PARENT_REFERENCED_RELATIONSHIP         = "parentReferencedRelationship";
  String CHILD_REFERENCED_NATURE_RELATIONSHIPS  = "childReferencedNatureRelationships";
  String CHILD_REFERENCED_RELATIONSHIP          = "childReferencedRelationship";
  
  public Map<String, IReferencedNatureRelationshipInheritanceModel> getParentReferencedNatureRelationships();
  
  public void setParentReferencedNatureRelationships(
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipInheritanceModel> getParentReferencedRelationship();
  
  public void setParentReferencedRelationship(
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship);
  
  public Map<String, IReferencedNatureRelationshipInheritanceModel> getChildReferencedNatureRelationships();
  
  public void setChildReferencedNatureRelationships(
      Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipInheritanceModel> getChildReferencedRelationship();
  
  public void setChildReferencedRelationship(
      Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship);
}
