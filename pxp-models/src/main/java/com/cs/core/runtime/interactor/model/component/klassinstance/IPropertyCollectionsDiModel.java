package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IPropertyCollectionsDiModel extends IModel {
  
  public static final String PROPERTY_COLLECTIONS = "propertyCollections";
  
  public Map<String, Object> getPropertyCollections();
  
  public void setPropertyCollections(Map<String, Object> attributes);
}
