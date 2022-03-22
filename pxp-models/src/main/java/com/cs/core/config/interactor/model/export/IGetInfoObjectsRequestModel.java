package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetInfoObjectsRequestModel extends IIdParameterModel, IModel {
  
  public static String OBJECT_TYPE_ATTRIBUTE_ID = "objectTypeAttributeId";
  public static String IMAGE_ATTRIBUTE_ID       = "imageAttributeId";
  public static String DEFAULT_LANGAUGE_ID      = "defaultLangaugeId";
  public static String LANGUAGE_MAP             = "languageMap";
  public static String OTHER_ATTRIBUTE_IDS      = "otherAttributeIds";
  
  public String getObjectTypeAttributeId();
  
  public void setObjectTypeAttributeId(String objectTypeAttributeId);
  
  public String getImageAttributeId();
  
  public void setImageAttributeId(String imageAttributeId);
  
  public String getDefaultLangaugeId();
  
  public void setDefaultLangaugeId(String defaultLanguageId);
  
  public Map<String, Object> getLanguageMap();
  
  public void setLanguageMap(Map<String, Object> languageMap);
  
  public List<String> getOtherAttributeIds();
  
  public void setOtherAttributeIds(List<String> otherAttributeIds);
}
