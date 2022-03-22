package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveTemplateModel extends ITemplate, IModel {
  
  public static final String MODIFIED_TEMPLATETABS       = "modifiedTemplateTabs";
  public static final String ADDED_PROPERTYCOLLECTIONS   = "addedPropertyCollections";
  public static final String DELETED_PROPERTYCOLLECTIONS = "deletedPropertyCollections";
  public static final String ADDED_RELATIONSHIPS         = "addedRelationships";
  public static final String DELETED_RELATIONSHIPS       = "deletedRelationships";
  public static final String MODIFIED_PROPERTYCOLLECTION = "modifiedPropertyCollection";
  public static final String MODIFIED_RELATIONSHIP       = "modifiedRelationship";
  
  public List<ITemplateTab> getModifiedTemplateTabs();
  
  public void setModifiedTemplateTabs(List<ITemplateTab> modifiedTemplateTabs);
  
  public List<String> getAddedPropertyCollections();
  
  public void setAddedPropertyCollections(List<String> addedPropertyCollections);
  
  public List<String> getDeletedPropertyCollections();
  
  public void setDeletedPropertyCollections(List<String> deletedPropertyCollections);
  
  public List<String> getAddedRelationships();
  
  public void setAddedRelationships(List<String> addedRelationships);
  
  public List<String> getDeletedRelationships();
  
  public void setDeletedRelationships(List<String> deletedRelationships);
  
  public IModifiedSequenceModel getModifiedPropertyCollection();
  
  public void setModifiedPropertyCollection(IModifiedSequenceModel modifiedPropertyCollection);
  
  public IModifiedSequenceModel getModifiedRelationship();
  
  public void setModifiedRelationship(IModifiedSequenceModel modifiedRelationship);
}
