package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveKlassInstanceRelationshipTreeStrategyModel extends IModel {
  
  public static final String ID                     = "id";
  public static final String TYPE                   = "type";
  public static final String ADDED_RELATIONSHIPS    = "addedRelationships";
  public static final String DELETED_RELATIONSHIPS  = "deletedRelationships";
  public static final String MODIFIED_RELATIONSHIPS = "modifiedRelationships";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public List<IContentRelationshipInstance> getAddedRelationships();
  
  public void setAddedRelationships(List<IContentRelationshipInstance> addedRelationships);
  
  public List<IContentRelationshipInstance> getDeletedRelationships();
  
  public void setDeletedRelationships(List<IContentRelationshipInstance> deletedRelationships);
  
  public List<IContentRelationshipInstance> getModifiedRelationships();
  
  public void setModifiedRelationships(List<IContentRelationshipInstance> modifiedRelationships);
}
