package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.component.jms.IDiModel;

import java.util.List;
import java.util.Map;

public interface IDiPropertiesModel extends IDiModel {
  
  public static String ATTRIBUTES         = "attributes";
  public static String TAGS               = "tags";
  public static String ATTRIBUTE_VARIANTS = "attributeVariants";
  
  public Map<String, String> getAttributes();
  
  public void setAttributes(Map<String, String> attributes);
  
  public Map<String, List<String>> getTags();
  
  public void setTags(Map<String, List<String>> tags);
  
  public List<IDiAttributeVariantModel> getAttributeVariants();
  
  public void setAttributeVariants(List<IDiAttributeVariantModel> attributeVariants);
}
