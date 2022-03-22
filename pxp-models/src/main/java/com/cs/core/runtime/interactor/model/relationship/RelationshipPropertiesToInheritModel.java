package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class RelationshipPropertiesToInheritModel implements IRelationshipPropertiesToInheritModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected String                         relationshipId;
  protected List<IDefaultValueChangeModel> attributes       = new ArrayList<>();
  protected List<IDefaultValueChangeModel> tags             = new ArrayList<>();
  protected String                         variantId;
  
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
  public List<IDefaultValueChangeModel> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setAttributes(List<IDefaultValueChangeModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setTags(List<IDefaultValueChangeModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(String variantId)
  {
    this.variantId = variantId;
  }
}
