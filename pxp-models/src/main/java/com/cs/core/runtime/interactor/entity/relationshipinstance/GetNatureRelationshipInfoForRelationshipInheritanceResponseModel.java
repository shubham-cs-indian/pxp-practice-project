package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetNatureRelationshipInfoForRelationshipInheritanceResponseModel implements IGetNatureRelationshipInfoForRelationshipInheritanceResponseModel{
private static final long serialVersionUID = 1L;
  
  protected Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships;
  protected Map<String, IReferencedRelationshipInheritanceModel>       referencedRelationship;
  protected Map<String, IReferencedRelationshipInheritanceModel>       referencedReference;
  
  @Override
  public Map<String, IReferencedNatureRelationshipInheritanceModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedNatureRelationshipInheritanceModel.class)
  public void setReferencedNatureRelationships(Map<String, IReferencedNatureRelationshipInheritanceModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IReferencedRelationshipInheritanceModel> getReferencedRelationship()
  {
    return referencedRelationship;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipInheritanceModel.class)
  public void setReferencedRelationship(Map<String, IReferencedRelationshipInheritanceModel> referencedRelationship)
  {
    this.referencedRelationship = referencedRelationship;
  }


}