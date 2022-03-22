package com.cs.core.runtime.interactor.model.component.klassinstance;

import java.util.HashMap;
import java.util.Map;

public class DiAttributeVariantPropertiesModel implements IDiAttributeVariantPropertiesModel {
  
  private static final long   serialVersionUID = 1L;
  private Map<String, Object> attributes;
  
  @Override
  public Map<String, Object> getAttributes()
  {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    return this.attributes;
  }
  
  @Override
  public void setAttributes(Map<String, Object> attributes)
  {
    this.attributes = attributes;
  }
}
