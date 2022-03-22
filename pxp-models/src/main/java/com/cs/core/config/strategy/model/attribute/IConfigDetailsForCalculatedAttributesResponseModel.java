package com.cs.core.config.strategy.model.attribute;

import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForCalculatedAttributesResponseModel extends IModel {

  public static final String REFERENCED_ATTRIBUTES                = "referencedAttributes";
  public static final String REFERENCED_ATTRIBUTES_FOR_CALCULATED = "referencedAttributesForCalculated";
  public static final String CLASSIFIER_IIDS_VS_PROPERTY_CODES    = "classifierIIDsVSPropertyCodes";

  public Map<String, IAttribute> getReferencedAttributes();
 
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public Map<Long, IAttribute> getReferencedAttributesForCalculated();
  
  public void setReferencedAttributesForCalculated(Map<Long, IAttribute> referencedAttributesForCalculated);
  
  public Map<Long, Set<String>> getClassifierIIDsVSPropertyCodes();
  
  public void setClassifierIIDsVSPropertyCodes(Map<Long, Set<String>> classifierIIDsVSPropertyCodes);
}
