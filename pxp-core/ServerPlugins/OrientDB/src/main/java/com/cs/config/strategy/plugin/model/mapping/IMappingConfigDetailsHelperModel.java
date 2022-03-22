package com.cs.config.strategy.plugin.model.mapping;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IMappingConfigDetailsHelperModel extends IModel {
  
  public static final String ATTRIBUTES          = "attributes";
  public static final String TAGS                = "tags";
  public static final String TAXONOMY            = "taxonomy";
  public static final String KLASSES             = "klasses";
  public static final String PROPERTYCOLLECTIONS = "propertyCollections";
  public static final String RELATIONSHIPS       = "relationships";
  public static final String CONTEXTS             = "contexts";
  
  public Map<String, Object> getAttributes();
  
  public void setAttributes(Map<String, Object> attributes);
  
  public Map<String, Object> getTags();
  
  public void setTags(Map<String, Object> tags);
  
  public Map<String, Object> getTaxonomy();
  
  public void setTaxonomy(Map<String, Object> taxonomy);
  
  public Map<String, Object> getKlasses();
  
  public void setKlasses(Map<String, Object> klasses);
  
  public void setPropertyCollections(Map<String, Object> propertyCollections);
  
  public Map<String, Object> getPropertyCollections();
  
  public Map<String, Object> getRelationships();
  
  public void setRelationships(Map<String, Object> relationships);
  
  public Map<String, Object> getContexts();
  
  public void setContexts(Map<String, Object> contexts);
}
