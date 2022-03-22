package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISideInfoForRelationshipInheritanceModel extends IModel {
  
  public static final String SIDE_ID                = "sideId";
  public static final String RELATIONSHIP_ID        = "relationshipId";
  public static final String MODIFIED_RELATIONSHIPS = "modifiedRelationships";
  public static final String DELETED_RELATIONSHIPS  = "deletedRelationships";
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<IModifiedRelationshipPropertyModel> getModifiedRelationships();
  
  public void setModifiedRelationships(
      List<IModifiedRelationshipPropertyModel> modifiedRelationships);
  
  public List<String> getDeletedRelationships();
  
  public void setDeletedRelationships(List<String> deletedRelationships);
}
