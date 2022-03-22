package com.cs.di.config.model.authorization;

import com.cs.core.config.interactor.entity.authorizationmapping.IAuthorizationMapping;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISavePartnerAuthorizationModel extends IAuthorizationMapping, IModel {
  
  public static final String ADDED_ATRRIBUTE_MAPPINGS      = "addedAttributeMappings";
  public static final String DELETED_ATTRIBUTE_MAPPINGS    = "deletedAttributeMappings";
  public static final String ADDED_TAG_MAPPINGS            = "addedTagMappings";
  public static final String DELETED_TAG_MAPPINGS          = "deletedTagMappings";
  public static final String ADDED_CLASS_MAPPINGS          = "addedClassMappings";
  public static final String DELETED_CLASS_MAPPINGS        = "deletedClassMappings";
  public static final String ADDED_TAXONOMY_MAPPINGS       = "addedTaxonomyMappings";
  public static final String DELETED_TAXONOMY_MAPPINGS     = "deletedTaxonomyMappings";
  public static final String ADDED_CONTEXT_MAPPINGS        = "addedContextMappings";
  public static final String DELETED_CONTEXT_MAPPINGS      = "deletedContextMappings";
  public static final String ADDED_RELATIONSHIP_MAPPINGS   = "addedRelationshipMappings";
  public static final String DELETED_RELATIONSHIP_MAPPINGS = "deletedRelationshipMappings";
  
  public List<String> getAddedAttributeMappings();
  
  public void setAddedAttributeMappings(List<String> addedAttributeMappings);
  
  public List<String> getDeletedAttributeMappings();
  
  public void setDeletedAttributeMappings(List<String> deletedAttributeMappings);
  
  public List<String> getAddedTagMappings();
  
  public void setAddedTagMappings(List<String> addedTagMappings);
  
  public List<String> getDeletedTagMappings();
  
  public void setDeletedTagMappings(List<String> deletedTagMappings);
  
  public List<String> getAddedClassMappings();
  
  public void setAddedClassMappings(List<String> addedClassMappings);
  
  public List<String> getDeletedClassMappings();
  
  public void setDeletedClassMappings(List<String> deletedClassMappings);
  
  public List<String> getAddedTaxonomyMappings();
  
  public void setAddedTaxonomyMappings(List<String> addedTaxonomyMappings);
  
  public List<String> getDeletedTaxonomyMappings();
  
  public void setDeletedTaxonomyMappings(List<String> deletedTaxonomyMappings);
  
  public List<String> getAddedContextMappings();
  
  public void setAddedContextMappings(List<String> addedContextMappings);
  
  public List<String> getDeletedContextMappings();
  
  public void setDeletedContextMappings(List<String> deletedContextMappings);
  
  public List<String> getAddedRelationshipMappings();
  
  public void setAddedRelationshipMappings(List<String> addedRelationshipMappings);
  
  public List<String> getDeletedRelationshipMappings();
  
  public void setDeletedRelationshipMappings(List<String> deletedRelationshipMappings);
}
