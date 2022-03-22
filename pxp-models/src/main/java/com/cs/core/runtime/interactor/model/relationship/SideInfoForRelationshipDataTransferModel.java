package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.ReferencedRelationshipProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SideInfoForRelationshipDataTransferModel
    implements ISideInfoForRelationshipDataTransferModel {
  
  private static final long                       serialVersionUID = 1L;
  protected String                                sideId;
  protected String                                relationshipId;
  protected List<IReferencedRelationshipProperty> modifiedDependentAttributes;
  protected List<IReferencedRelationshipProperty> modifiedInDependentAttributes;
  protected List<IReferencedRelationshipProperty> modifiedTags;
  protected List<String>                          deletedDependentAttributes;
  protected List<String>                          deletedInDependentAttributes;
  protected List<String>                          deletedTags;
  
  @Override
  public List<IReferencedRelationshipProperty> getModifiedDependentAttributes()
  {
    if (modifiedDependentAttributes == null) {
      modifiedDependentAttributes = new ArrayList<>();
    }
    return modifiedDependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  public void setModifiedDependentAttributes(
      List<IReferencedRelationshipProperty> modifiedDependentAttributes)
  {
    this.modifiedDependentAttributes = modifiedDependentAttributes;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getModifiedInDependentAttributes()
  {
    if (modifiedInDependentAttributes == null) {
      modifiedInDependentAttributes = new ArrayList<>();
    }
    return modifiedInDependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  public void setModifiedInDependentAttributes(
      List<IReferencedRelationshipProperty> modifiedInDependentAttributes)
  {
    this.modifiedInDependentAttributes = modifiedInDependentAttributes;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<>();
    }
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  public void setModifiedTags(List<IReferencedRelationshipProperty> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<String> getDeletedDependentAttributes()
  {
    if (deletedDependentAttributes == null) {
      deletedDependentAttributes = new ArrayList<>();
    }
    return deletedDependentAttributes;
  }
  
  @Override
  public void setDeletedDependentAttributes(List<String> deletedDependentAttributes)
  {
    this.deletedDependentAttributes = deletedDependentAttributes;
  }
  
  @Override
  public List<String> getDeletedInDependentAttributes()
  {
    if (deletedInDependentAttributes == null) {
      deletedInDependentAttributes = new ArrayList<>();
    }
    return deletedInDependentAttributes;
  }
  
  @Override
  public void setDeletedInDependentAttributes(List<String> deletedInDependentAttributes)
  {
    this.deletedInDependentAttributes = deletedInDependentAttributes;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<>();
    }
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
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
}
