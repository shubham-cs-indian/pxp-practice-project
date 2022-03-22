package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IDiAttributeVariantPropertiesModel extends IModel {
  
  public static String ATTRIBUTES = "attributes";
  
  public Map<String, Object> getAttributes();
  
  public void setAttributes(Map<String, Object> attributes);
}
