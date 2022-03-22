package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AttributeDiffModel implements IAttributeDiffModel {
  
  protected String          id;
  // protected String mappingId;
  protected String          oldValue;
  protected String          newValue;
  protected String          attributeId;
  protected ISectionElement attribute;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  /*  @JsonProperty("attributeMappingId")
  @Override
  public String getMappingId()
  {
    return mappingId;
  }
  
  @JsonProperty("attributeMappingId")
  @Override
  public void setMappingId(String mappingId)
  {
    this.mappingId = mappingId;
  }*/
  
  @Override
  public String getOldValue()
  {
    return oldValue;
  }
  
  @Override
  public void setOldValue(String oldValue)
  {
    this.oldValue = oldValue;
  }
  
  @Override
  public String getNewValue()
  {
    return newValue;
  }
  
  @Override
  public void setNewValue(String newValue)
  {
    this.newValue = newValue;
  }
  
  @Override
  @JsonIgnore
  public ISectionElement getSectionElement()
  {
    return attribute;
  }
  
  @Override
  public void setSectionElement(ISectionElement attribute)
  {
    this.attribute = attribute;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
}
