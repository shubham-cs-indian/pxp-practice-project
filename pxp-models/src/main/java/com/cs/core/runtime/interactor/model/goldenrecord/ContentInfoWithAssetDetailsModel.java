package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.Map;

public class ContentInfoWithAssetDetailsModel implements IContentInfoWithAssetDetailsModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              id;
  protected String              defaultAssetInstanceId;
  protected String              name;
  protected String              baseType;
  protected String              type;
  protected String              thumbKey;
  protected Map<String, String> properties;
  
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
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getThumbKey()
  {
    return thumbKey;
  }
  
  @Override
  public void setThumbKey(String thumbKey)
  {
    this.thumbKey = thumbKey;
  }
  
  @Override
  public Map<String, String> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }
}
