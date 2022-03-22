package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISideInfoForRelationshipDataTransferModel extends IModel {
  
  public static final String MODIFIED_DEPENDENT_ATTRIBUTES   = "modifiedDependentAttributes";
  public static final String MODIFIED_INDEPENDENT_ATTRIBUTES = "modifiedInDependentAttributes";
  public static final String MODIFIED_TAGS                   = "modifiedTags";
  public static final String DELETED_DEPENDENT_ATTRIBUTES    = "deletedDependentAttributes";
  public static final String DELETED_INDEPENDENT_ATTRIBUTES  = "deletedInDependentAttributes";
  public static final String DELETED_TAGS                    = "deletedTags";
  public static final String SIDE_ID                         = "sideId";
  public static final String RELATIONSHIP_ID                 = "relationshipId";
  
  public List<IReferencedRelationshipProperty> getModifiedDependentAttributes();
  
  public void setModifiedDependentAttributes(
      List<IReferencedRelationshipProperty> modifiedDependentAttributes);
  
  public List<IReferencedRelationshipProperty> getModifiedInDependentAttributes();
  
  public void setModifiedInDependentAttributes(
      List<IReferencedRelationshipProperty> modifiedInDependentAttributes);
  
  public List<IReferencedRelationshipProperty> getModifiedTags();
  
  public void setModifiedTags(List<IReferencedRelationshipProperty> modifiedTags);
  
  public List<String> getDeletedDependentAttributes();
  
  public void setDeletedDependentAttributes(List<String> deletedDependentAttributes);
  
  public List<String> getDeletedInDependentAttributes();
  
  public void setDeletedInDependentAttributes(List<String> deletedInDependentAttributes);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public String getSideId();
  
  public void setSideId(String sideId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
}
