package com.cs.di.config.model.authorization;

import com.cs.core.config.interactor.entity.authorizationmapping.IAuthorizationMapping;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

import java.util.List;

public interface IPartnerAuthorizationModel extends IAuthorizationMapping, IConfigModel {
  
  public static final String CONTEXT_MAPPINGS      = "contextMappings";
  public static final String RELATIONSHIP_MAPPINGS = "relationshipMappings";
  public static final String ATTRIBUTE_MAPPINGS    = "attributeMappings";
  public static final String TAG_MAPPINGS          = "tagMappings";
  public static final String CLASS_MAPPINGS        = "classMappings";
  public static final String TAXONOMY_MAPPINGS     = "taxonomyMappings";
  
  public List<String> getAttributeMappings();
  
  public void setAttributeMappings(List<String> attributeMappings);
  
  public List<String> getTagMappings();
  
  public void setTagMappings(List<String> tagMappings);
  
  public List<String> getClassMappings();
  
  public void setClassMappings(List<String> classMappings);
  
  public List<String> getTaxonomyMappings();
  
  public void setTaxonomyMappings(List<String> taxonomyMappings);
  
  public List<String> getContextMappings();
  
  public void setContextMappings(List<String> contextMappings);
  
  public List<String> getRelationshipMappings();
  
  public void setRelationshipMappings(List<String> relationshipMappings);
}
