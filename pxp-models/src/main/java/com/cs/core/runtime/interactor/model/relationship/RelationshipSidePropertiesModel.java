package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.ReferencedRelationshipProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class RelationshipSidePropertiesModel implements IRelationshipSidePropertiesModel {
  
  private static final long                       serialVersionUID = 1L;
  
  protected List<String>                          klassIds         = new ArrayList<>();
  protected String                                targetType;
  protected List<IReferencedRelationshipProperty> attributes       = new ArrayList<>();
  protected List<IReferencedRelationshipProperty> tags             = new ArrayList<>();
  protected String                                sideId;
  protected List<IReferencedRelationshipProperty> dependentAttributes;
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public String getTargetType()
  {
    return targetType;
  }
  
  @Override
  public void setTargetType(String targetType)
  {
    this.targetType = targetType;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  @Override
  public void setAttributes(List<IReferencedRelationshipProperty> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IReferencedRelationshipProperty> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  @Override
  public void setTags(List<IReferencedRelationshipProperty> tags)
  {
    this.tags = tags;
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
  public List<IReferencedRelationshipProperty> getDependentAttributes()
  {
    if (dependentAttributes == null) {
      dependentAttributes = new ArrayList<>();
    }
    return dependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipProperty.class)
  public void setDependentAttributes(List<IReferencedRelationshipProperty> dependentAttributes)
  {
    this.dependentAttributes = dependentAttributes;
  }
}
