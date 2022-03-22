package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IDiEntityListModel extends IModel {
  
  public static String ATTRIBUTE_IDS    = "attributeIds";
  public static String TAG_IDS          = "tagIds";
  public static String CLASS_IDS        = "classIds";
  public static String TAXONOMY_IDS     = "taxonomyIds";
  public static String CONTEXT_IDS      = "contextIds";
  public static String RELATIONSHIP_IDS = "relationshipIds";
  
  public Set<String> getAttributeIds();
  
  public void setAttributeIds(Set<String> attributeIds);
  
  public Set<String> getTagIds();
  
  public void setTagIds(Set<String> tagIds);
  
  public Set<String> getClassIds();
  
  public void setClassIds(Set<String> classIds);
  
  public Set<String> getTaxonomyIds();
  
  public void setTaxonomyIds(Set<String> taxonomyIds);
  
  public Set<String> getContextIds();
  
  public void setContextIds(Set<String> contextIds);
  
  public Set<String> getRelationshipIds();
  
  public void setRelationshipIds(Set<String> relationshipIds);
}
