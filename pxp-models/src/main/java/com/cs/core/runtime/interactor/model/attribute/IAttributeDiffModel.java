package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IAttributeDiffModel extends IModel {
  
  public static final String ID              = "id";
  public static final String OLD_VALUE       = "oldValue";
  public static final String NEW_VALUE       = "newValue";
  public static final String SECTION_ELEMENT = "sectionElement";
  public static final String ATTRIBUTE_ID    = "attributeId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getOldValue();
  
  public void setOldValue(String oldValue);
  
  public String getNewValue();
  
  public void setNewValue(String newValue);
  
  public ISectionElement getSectionElement();
  
  public void setSectionElement(ISectionElement sectionElement);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
}
