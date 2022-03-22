package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;

import java.util.List;
import java.util.Map;

public class GetReferenceToMamObjectsRequestModel extends IdParameterModel
    implements IGetInfoObjectsRequestModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              objectTypeAttributeId;
  protected String              imageAttributeId;
  protected String              defaultLangaugeId;
  protected Map<String, Object> languageMap;
  protected List<String>        otherAttributeIds;
  
  @Override
  public String getObjectTypeAttributeId()
  {
    return objectTypeAttributeId;
  }
  
  @Override
  public void setObjectTypeAttributeId(String objectTypeAttributeId)
  {
    this.objectTypeAttributeId = objectTypeAttributeId;
  }
  
  @Override
  public String getImageAttributeId()
  {
    return imageAttributeId;
  }
  
  @Override
  public void setImageAttributeId(String imageAttributeId)
  {
    this.imageAttributeId = imageAttributeId;
  }
  
  @Override
  public String getDefaultLangaugeId()
  {
    return defaultLangaugeId;
  }
  
  @Override
  public void setDefaultLangaugeId(String defaultLangaugeId)
  {
    this.defaultLangaugeId = defaultLangaugeId;
  }
  
  @Override
  public Map<String, Object> getLanguageMap()
  {
    return languageMap;
  }
  
  @Override
  public void setLanguageMap(Map<String, Object> languageMap)
  {
    this.languageMap = languageMap;
  }
  
  @Override
  public List<String> getOtherAttributeIds()
  {
    return otherAttributeIds;
  }
  
  @Override
  public void setOtherAttributeIds(List<String> otherAttributeIds)
  {
    this.otherAttributeIds = otherAttributeIds;
  }
}
