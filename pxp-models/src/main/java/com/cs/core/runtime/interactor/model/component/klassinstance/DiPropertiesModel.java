package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiPropertiesModel implements IDiPropertiesModel {
  
  private static final long              serialVersionUID = 1L;
  
  private Map<String, String>            attributes;
  private Map<String, List<String>>      tags;
  private List<IDiAttributeVariantModel> attributeVariants;
  
  @Override
  public Map<String, String> getAttributes()
  {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    return this.attributes;
  }
  
  @Override
  public void setAttributes(Map<String, String> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, List<String>> getTags()
  {
    if (this.tags == null) {
      this.tags = new HashMap<>();
    }
    return this.tags;
  }
  
  @Override
  public void setTags(Map<String, List<String>> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IDiAttributeVariantModel> getAttributeVariants()
  {
    if (this.attributeVariants == null) {
      this.attributeVariants = new ArrayList<>();
    }
    return attributeVariants;
  }
  
  @Override
  @JsonDeserialize(contentAs = DiAttributeVariantModel.class)
  public void setAttributeVariants(List<IDiAttributeVariantModel> attributeVariants)
  {
    this.attributeVariants = attributeVariants;
  }
}
