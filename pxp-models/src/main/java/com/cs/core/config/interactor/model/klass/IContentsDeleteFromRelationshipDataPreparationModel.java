package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IContentsDeleteFromRelationshipDataPreparationModel extends IModel {
  
  public static final String SOURCE_CONTENT_ID = "sourceContentId";
  public static final String RELATIONSHIP_ID   = "relationshipId";
  public static final String BASE_TYPE         = "baseType";
  public static final String DELETED_ELEMENTS  = "deletedElements";
  public static final String PROPERTY_IDS      = "propertyIds";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getDeletedElements();
  
  public void setDeletedElements(List<String> deletedElements);
  
  public List<String> getPropertyIds();
  
  public void setPropertyIds(List<String> propertyIds);
}
