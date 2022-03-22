package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.clone.IDataForInstanceCloneModel;

import java.util.ArrayList;
import java.util.List;

public class DataForInstanceCloneModel implements IDataForInstanceCloneModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    attributes;
  protected List<String>    tags;
  protected List<String>    roles;
  protected List<String>    relationships;
  protected List<String>    contexts;
  protected List<String>    types;
  protected List<String>    taxonomies;
  
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
  public List<String> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<String>();
    }
    return roles;
  }
  
  @Override
  public void setRoles(List<String> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public List<String> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<String>();
    }
    return relationships;
  }
  
  @Override
  public void setRelationships(List<String> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<String> getContexts()
  {
    if (contexts == null) {
      contexts = new ArrayList<String>();
    }
    return contexts;
  }
  
  @Override
  public void setContexts(List<String> contexts)
  {
    this.contexts = contexts;
  }
  
  @Override
  public List<String> getTypes()
  {  
  if(types == null) {  
    types = new ArrayList<>();
  }
    return types;
  }

  @Override
  public void setTypes(List<String> types)
  {
   this.types = types;
    
  }

  @Override
  public List<String> getTaxonomies()
  {
   if(taxonomies == null) {
     taxonomies = new ArrayList<>();
   }
    return taxonomies;
  }

  @Override
  public void setTaxonomies(List<String> taxonomies)
  {
    this.taxonomies = taxonomies;
    
  }
}
