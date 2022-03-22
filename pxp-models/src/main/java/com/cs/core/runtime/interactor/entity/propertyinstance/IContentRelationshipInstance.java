package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import java.util.List;

public interface IContentRelationshipInstance extends IEntity {
  
  // TODO: Refactoring needed
  public static final String RELATIONSHIP_ID   = "relationshipId";
  public static final String SIDE_ID           = "sideId";
  
  public static final String ADDED_ELEMENTS    = "addedElements";
  
  public static final String DELETED_ELEMENTS  = "deletedElements";
  public static final String MODIFIED_ELEMENTS = "modifiedElements";
  
  public static final String ELEMENT_IDS       = "elementIds";
  public static final String MODIFIED_CONTEXTS = "modifiedContexts";
  public static final String BASE_TYPE         = "baseType";
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public List<IRelationshipVersion> getElementIds();
  
  public void setElementIds(List<IRelationshipVersion> elementIds);
  
  public List<IRelationshipVersion> getAddedElements();
  
  public void setAddedElements(List<IRelationshipVersion> addedElements);
  
  public List<String> getDeletedElements();
  
  public void setDeletedElements(List<String> deletedElements);
  
  public List<IRelationshipVersion> getModifiedElements();
  
  public void setModifiedElements(List<IRelationshipVersion> modifiedElements);
  
  public List<IRelationshipVersion> getModifiedContexts();
  
  public void setModifiedContexts(List<IRelationshipVersion> modifiedContexts);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getSideId();
  
  public void setSideId(String sideId);
}
