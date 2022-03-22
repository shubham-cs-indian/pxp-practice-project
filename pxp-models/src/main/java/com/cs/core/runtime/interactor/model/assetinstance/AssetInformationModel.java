package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.HashMap;

public class AssetInformationModel implements IAssetInformationModel {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected String                  thumbKey;
  protected String                  assetObjectKey;
  protected HashMap<String, String> properties;
  protected String                  type;
  protected String                  fileName;
  protected String                  previewImageKey;
  protected String                  hash;
  protected Long                    lastModified;
  protected String                  filePath;
  protected String                  thumbnailPath;
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
  public String getHash()
  {
    return hash;
  }
  
  @Override
  public void setHash(String hash)
  {
    this.hash = hash;
  }
  
  @Override
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }

  
  public String getFilePath()
  {
    return filePath;
  }

  
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }

  
  public String getThumbnailPath()
  {
    return thumbnailPath;
  }

  
  public void setThumbnailPath(String thumbnailPath)
  {
    this.thumbnailPath = thumbnailPath;
  }
  
 
}
