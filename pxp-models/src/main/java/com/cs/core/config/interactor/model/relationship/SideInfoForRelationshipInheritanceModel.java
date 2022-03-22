package com.cs.core.config.interactor.model.relationship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SideInfoForRelationshipInheritanceModel
    implements ISideInfoForRelationshipInheritanceModel {
  
  private static final long                          serialVersionUID = 1L;
  protected String                                   sideId;
  protected String                                   relationshipId;
  protected List<IModifiedRelationshipPropertyModel> modifiedRelationships;
  protected List<String>                             deletedRelationships;
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
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
  public List<IModifiedRelationshipPropertyModel> getModifiedRelationships()
  {
    if (modifiedRelationships == null) {
      modifiedRelationships = new ArrayList<>();
    }
    return modifiedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  public void setModifiedRelationships(
      List<IModifiedRelationshipPropertyModel> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
  
  @Override
  public List<String> getDeletedRelationships()
  {
    if (deletedRelationships == null) {
      deletedRelationships = new ArrayList<>();
    }
    return deletedRelationships;
  }
  
  @Override
  public void setDeletedRelationships(List<String> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
}
