package com.cs.core.runtime.interactor.model.clone;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDataForInstanceCloneModel extends IModel {
  
  public static final String ATTRIBUTES    = "attributes";
  public static final String TAGS          = "tags";
  public static final String ROLES         = "roles";
  public static final String RELATIONSHIPS = "relationships";
  public static final String CONTEXTS      = "contexts";
  public static final String TYPES         = "types";
  public static final String TAXONOMIES    = "taxonomies";
  
  public List<String> getAttributes();
  
  public void setAttributes(List<String> attributes);
  
  public List<String> getTags();
  
  public void setTags(List<String> tags);
  
  public List<String> getRoles();
  
  public void setRoles(List<String> roles);
  
  public List<String> getRelationships();
  
  public void setRelationships(List<String> relationships);
  
  public List<String> getContexts();
  
  public void setContexts(List<String> contexts);

  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<String> getTaxonomies();
  
  public void setTaxonomies(List<String> taxonomies);
}
