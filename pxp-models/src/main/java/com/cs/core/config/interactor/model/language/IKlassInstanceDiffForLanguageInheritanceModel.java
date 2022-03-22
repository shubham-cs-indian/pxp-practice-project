package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IKlassInstanceDiffForLanguageInheritanceModel extends IModel {
  
  public static final String CONTENT_ID          = "contentId";
  public static final String BASE_TYPE           = "baseType";
  public static final String ADDED_ATTRIBUTES    = "addedAttributes";
  public static final String MODIFIED_ATTRIBUTES = "modifiedAttributes";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Map<String, List<String>> getAddedAttributes();
  
  public void setAddedAttributes(Map<String, List<String>> addedAttributes);
  
  public Map<String, List<String>> getModifiedAttributes();
  
  public void setModifiedAttributes(Map<String, List<String>> modifiedAttributes);
}
