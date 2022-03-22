package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.HashMap;

public class ReferenceAssetModel implements IReferenceAssetModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected String                  thumbKey;
  
  protected String                  assetObjectKey;
  
  protected HashMap<String, String> properties;
  
  protected String                  type;
  
  protected String                  previewImageKey;
  
  protected String                  fileName;
  
  protected String                  description;
  
  protected Boolean                 isDefault;
  
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
  public String getAssetObjectKey()
  {
    return assetObjectKey;
  }
  
  @Override
  public void setAssetObjectKey(String assetObjectKey)
  {
    this.assetObjectKey = assetObjectKey;
  }
  
  @Override
  public HashMap<String, String> getProperties()
  {
    return properties;
  }
  
  @Override
  public void setProperties(HashMap<String, String> properties)
  {
    this.properties = properties;
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
  public String getPreviewImageKey()
  {
    return previewImageKey;
  }
  
  @Override
  public void setPreviewImageKey(String previewImageKey)
  {
    this.previewImageKey = previewImageKey;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public Boolean getIsDefault()
  {
    
    return isDefault;
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }
}
