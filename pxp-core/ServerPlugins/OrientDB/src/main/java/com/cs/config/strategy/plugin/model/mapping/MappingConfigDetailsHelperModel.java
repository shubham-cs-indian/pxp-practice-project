package com.cs.config.strategy.plugin.model.mapping;

import java.util.HashMap;
import java.util.Map;

public class MappingConfigDetailsHelperModel implements IMappingConfigDetailsHelperModel {
  
  private static final long     serialVersionUID    = 1L;
  protected Map<String, Object> attributes          = new HashMap<String, Object>();
  protected Map<String, Object> tags                = new HashMap<String, Object>();
  protected Map<String, Object> taxonomy            = new HashMap<String, Object>();
  protected Map<String, Object> klasses             = new HashMap<String, Object>();
  protected Map<String, Object> propertyCollections = new HashMap<String, Object>();
  protected Map<String, Object> relationships       = new HashMap<String, Object>();
  protected Map<String, Object> contexts             = new HashMap<String, Object>(); 
  
  @Override
  public Map<String, Object> getAttributes()
  {
    return attributes;
  }
  
  @Override
  public void setAttributes(Map<String, Object> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, Object> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(Map<String, Object> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Map<String, Object> getTaxonomy()
  {
    return taxonomy;
  }
  
  @Override
  public void setTaxonomy(Map<String, Object> taxonomy)
  {
    this.taxonomy = taxonomy;
  }
  
  @Override
  public Map<String, Object> getKlasses()
  {
    return klasses;
  }
  
  @Override
  public void setKlasses(Map<String, Object> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public Map<String, Object> getPropertyCollections()
  {
    return propertyCollections;
  }
  
  @Override
  public void setPropertyCollections(Map<String, Object> propertyCollections)
  {
    this.propertyCollections = propertyCollections;
  }
  
  @Override
  public Map<String, Object> getRelationships()
  {
    return relationships;
  }
  
  @Override
  public void setRelationships(Map<String, Object> relationships)
  {
    this.relationships = relationships;
  }

  @Override
  public Map<String, Object> getContexts()
  {
    return contexts;
  }

  @Override
  public void setContexts(Map<String, Object> contexts)
  {
    this.contexts = contexts;
  }
}
