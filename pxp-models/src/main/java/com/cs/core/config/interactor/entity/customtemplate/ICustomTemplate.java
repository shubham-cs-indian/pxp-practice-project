package com.cs.core.config.interactor.entity.customtemplate;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface ICustomTemplate extends IConfigMasterEntity {
  
  public static final String PROPERTY_COLLECTION_IDS = "propertyCollectionIds";
  public static final String RELATIONSHIP_IDS        = "relationshipIds";
  public static final String NATURE_RELATIONSHIP_IDS = "natureRelationshipIds";
  public static final String CONTEXT_IDS             = "contextIds";
  
  public List<String> getPropertyCollectionIds();
  
  public void setPropertyCollectionIds(List<String> propertyCollectionIds);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public List<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(List<String> natureRelationshipIds);
  
  public List<String> getContextIds();
  
  public void setContextIds(List<String> contextIds);
}
