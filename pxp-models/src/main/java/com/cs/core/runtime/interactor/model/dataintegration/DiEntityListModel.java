package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.HashSet;
import java.util.Set;

public class DiEntityListModel implements IDiEntityListModel {
  
  private static final long serialVersionUID = 1L;
  private Set<String>       attributeIds     = new HashSet<>();
  private Set<String>       tagIds           = new HashSet<>();
  private Set<String>       classIds         = new HashSet<>();
  private Set<String>       taxonomyIds      = new HashSet<>();
  private Set<String>       contextIds       = new HashSet<>();
  private Set<String>       relationshipIds  = new HashSet<>();
  
  @Override
  public Set<String> getAttributeIds()
  {
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(Set<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public Set<String> getTagIds()
  {
    return tagIds;
  }
  
  @Override
  public void setTagIds(Set<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public Set<String> getClassIds()
  {
    return classIds;
  }
  
  @Override
  public void setClassIds(Set<String> classIds)
  {
    this.classIds = classIds;
  }
  
  @Override
  public Set<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(Set<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public Set<String> getContextIds()
  {
    return contextIds;
  }
  
  @Override
  public void setContextIds(Set<String> contextIds)
  {
    this.contextIds = contextIds;
  }
  
  @Override
  public Set<String> getRelationshipIds()
  {
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(Set<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
}
