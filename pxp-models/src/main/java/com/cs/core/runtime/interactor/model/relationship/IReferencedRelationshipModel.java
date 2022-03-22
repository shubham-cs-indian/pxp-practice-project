package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.config.interactor.entity.relationship.IRelationship;

import java.util.List;

public interface IReferencedRelationshipModel extends IRelationship {
  
  public static final String TAGS       = "tags";
  public static final String ATTRIBUTES = "attributes";
  
  public List<String> getTags();
  
  public void setTags(List<String> tags);
  
  public List<String> getAttributes();
  
  public void setAttributes(List<String> attributes);
}
