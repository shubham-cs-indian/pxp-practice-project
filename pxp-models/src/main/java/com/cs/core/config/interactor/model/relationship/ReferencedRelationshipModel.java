package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;

import java.util.ArrayList;
import java.util.List;

public class ReferencedRelationshipModel extends Relationship
    implements IReferencedRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    tags;
  protected List<String>    attributes;
  
  @Override
  public List<String> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<String>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<String> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<String> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<String>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<String> attributes)
  {
    this.attributes = attributes;
  }
}
