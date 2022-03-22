package com.cs.core.config.interactor.model.customtemplate;

import com.cs.core.config.interactor.entity.customtemplate.ICustomTemplate;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveCustomTemplateModel extends ICustomTemplate, IModel {
  
  public static final String ADDED_PROPERTYCOLLECTIONS    = "addedPropertyCollections";
  public static final String DELETED_PROPERTYCOLLECTIONS  = "deletedPropertyCollections";
  public static final String ADDED_RELATIONSHIPS          = "addedRelationships";
  public static final String DELETED_RELATIONSHIPS        = "deletedRelationships";
  public static final String ADDED_NATURE_RELATIONSHIPS   = "addedNatureRelationships";
  public static final String DELETED_NATURE_RELATIONSHIPS = "deletedNatureRelationships";
  public static final String ADDED_CONTEXTS               = "addedContexts";
  public static final String DELETED_CONTEXTS             = "deletedContexts";
  
  public List<String> getAddedPropertyCollections();
  
  public void setAddedPropertyCollections(List<String> addedPropertyCollections);
  
  public List<String> getDeletedPropertyCollections();
  
  public void setDeletedPropertyCollections(List<String> deletedPropertyCollections);
  
  public List<String> getAddedRelationships();
  
  public void setAddedRelationships(List<String> addedRelationships);
  
  public List<String> getDeletedRelationships();
  
  public void setDeletedRelationships(List<String> deletedRelationships);
  
  public List<String> getAddedNatureRelationships();
  
  public void setAddedNatureRelationships(List<String> addedNatureRelationships);
  
  public List<String> getDeletedNatureRelationships();
  
  public void setDeletedNatureRelationships(List<String> deletedNatureRelationships);
  
  public List<String> getAddedContexts();
  
  public void setAddedContexts(List<String> addedContexts);
  
  public List<String> getDeletedContexts();
  
  public void setDeletedContexts(List<String> deletedContexts);
  
}
