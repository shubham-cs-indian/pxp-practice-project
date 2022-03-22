package com.cs.core.config.strategy.model.attribute;

import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForCalculatedAttributesResponseModel implements IConfigDetailsForCalculatedAttributesResponseModel{

  protected Map<String, IAttribute> referencedAttributes;
  protected Map<Long, IAttribute>   referencedAttributesForCalculated;
  protected Map<Long, Set<String>>  classifierIIDsVSPropertyCodes;
 
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<Long, IAttribute> getReferencedAttributesForCalculated()
  {
    return referencedAttributesForCalculated;
  }

  @Override
  public void setReferencedAttributesForCalculated(Map<Long, IAttribute> referencedAttributesForCalculated)
  {
    this.referencedAttributesForCalculated = referencedAttributesForCalculated;
  }

  @Override
  public Map<Long, Set<String>> getClassifierIIDsVSPropertyCodes()
  {
    return classifierIIDsVSPropertyCodes;
  }

  @Override
  public void setClassifierIIDsVSPropertyCodes(Map<Long, Set<String>> classifierIIDsVSPropertyCodes)
  {
    this.classifierIIDsVSPropertyCodes = classifierIIDsVSPropertyCodes;
  }
  
}
