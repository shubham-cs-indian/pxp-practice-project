package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.HashMap;

public class AssetInfoModel implements IAssetInfoModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected String                  thumbKey;
  protected String                  filename;
  protected String                  type;
  protected HashMap<String, String> properties;
  protected String                  assetObjectKey;
  protected String                  previewKey;
  
  @Override
  public String getPreviewKey()
  {
    return previewKey;
  }
  
  @Override
  public void setPreviewKey(String previewKey)
  {
    this.previewKey = previewKey;
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
  public String getFilename()
  {
    return filename;
  }
  
  @Override
  public void setFilename(String filename)
  {
    this.filename = filename;
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
  public HashMap<String, String> getProperties()
  {
    if(properties == null) {
      properties = new HashMap<>();
    }
    return properties;
  }
  
  @Override
  public void setProperties(HashMap<String, String> properties)
  {
    this.properties = properties;
  }
}
