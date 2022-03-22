package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;

public interface IRelationshipInheritanceOnTypeSwitchRequestModel extends IModel {
  
  public static final String CONTENT_ID          = "contentId";
  public static final String BASETYPE            = "baseType";
  public static final String EXISTING_TYPES      = "existingTypes";
  public static final String EXISTING_TAXONOMIES = "existingTaxonomies";
  public static final String ADDED_TYPES         = "addedTypes";
  public static final String ADDED_TAXONOMIES    = "addedTaxonomies";
  // public static final String REMOVED_TYPE = "removedType";
  public static final String REMOVED_TYPES       = "removedTypes";
  public static final String REMOVED_TAXONOMIES  = "removedTaxonomies";
  
  public String getContentId();
  
  public void setContentId(String sourceContentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getExistingTypes();
  
  public void setExistingTypes(List<String> existingTypes);
  
  public List<String> getExistingTaxonomies();
  
  public void setExistingTaxonomies(List<String> existingTaxonomies);
  
  public List<String> getAddedTypes();
  
  public void setAddedTypes(List<String> addedTypes);
  
  public List<String> getAddedTaxonomies();
  
  public void setAddedTaxonomies(List<String> addedTaxonomies);
  
  public List<String> getRemovedTypes();
  
  public void setRemovedTypes(List<String> removedTypes);
  
  public List<String> getRemovedTaxonomies();
  
  public void setRemovedTaxonomies(List<String> removedTaxonomy);
}
