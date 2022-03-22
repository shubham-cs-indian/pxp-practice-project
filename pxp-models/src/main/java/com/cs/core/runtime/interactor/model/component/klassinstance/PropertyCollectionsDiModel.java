package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.HashMap;
import java.util.Map;

public class PropertyCollectionsDiModel implements IPropertyCollectionsDiModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Map<String, Object> propertyCollections;
  
  @Override
  @JsonAnyGetter
  public Map<String, Object> getPropertyCollections()
  {
    if (this.propertyCollections == null) {
      this.propertyCollections = new HashMap<String, Object>();
    }
    return this.propertyCollections;
  }
  
  @Override
  public void setPropertyCollections(Map<String, Object> propertyCollections)
  {
    this.propertyCollections = propertyCollections;
  }
}
