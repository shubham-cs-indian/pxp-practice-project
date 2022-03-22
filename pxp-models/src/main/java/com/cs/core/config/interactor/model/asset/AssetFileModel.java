package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;

public class AssetFileModel extends IdsListParameterModel implements IAssetFileModel {
  
  private static final long              serialVersionUID = 1L;
  protected String                       container;
  protected byte[]                       bytes;
  protected String                       name;
  protected String                       extension;
  protected String                       path;
  protected String                       key;
  protected String                       assetObjectKey;
  protected Boolean                      shouldCheckForRedundancy;
  protected String                       thumbKey;
  protected IAssetExtensionConfiguration extensionConfiguration;
  protected String                       extensionType;
  protected String                       klassId;
  protected Boolean                      isSynchronus     = false;
  protected Boolean                      isExtracted      = false;
  protected String                       code;
  protected String                       thumbnailPath;
  protected boolean                      isInDesignServerEnabled;
  
  public AssetFileModel(String container, byte[] bytes, String name, String extension, String path,
      String key, Boolean shouldCheckForRedundancy, String assetObjectKey,
      IAssetExtensionConfiguration extensionConfiguration, String extensionType, String klassId, 
      String code)
  {
    this.container = container;
    this.bytes = bytes;
    this.name = name;
    this.extension = extension;
    this.path = path;
    this.key = key;
    this.shouldCheckForRedundancy = shouldCheckForRedundancy;
    this.assetObjectKey = assetObjectKey;
    this.extensionConfiguration = extensionConfiguration;
    this.extensionType = extensionType;
    this.klassId = klassId;
    this.code = code;
  }
  
  @Override
  public String getContainer()
  {
    return container;
  }
  
  @Override
  public void setContainer(String container)
  {
    this.container = container;
  }
  
  @Override
  public byte[] getBytes()
  {
    return bytes;
  }
  
  @Override
  public void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
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
  public String getExtension()
  {
    return extension;
  }
  
  @Override
  public void setExtension(String extension)
  {
    this.extension = extension;
  }
  
  @Override
  public String getPath()
  {
    return path;
  }
  
  @Override
  public void setPath(String path)
  {
    this.path = path;
  }
  
  @Override
  public String getKey()
  {
    return key;
  }
  
  @Override
  public void setKey(String key)
  {
    this.key = key;
  }
  
  @Override
  public Boolean getShouldCheckForRedundancy()
  {
    return shouldCheckForRedundancy;
  }
  
  @Override
  public void setShouldCheckForRedundancy(Boolean shouldCheckForRedundancy)
  {
    this.shouldCheckForRedundancy = shouldCheckForRedundancy;
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
  public IAssetExtensionConfiguration getExtensionConfiguration()
  {
    return extensionConfiguration;
  }
  
  @Override
  public void setExtensionConfiguration(IAssetExtensionConfiguration extensionConfiguration)
  {
    this.extensionConfiguration = extensionConfiguration;
  }
  
  @Override
  public String getExtensionType()
  {
    return extensionType;
  }
  
  @Override
  public void setExtensionType(String extensionType)
  {
    this.extensionType = extensionType;
  }
  
  @Override
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public Boolean getIsSynchronus()
  {
    return isSynchronus;
  }
  
  @Override
  public void setIsSynchronus(Boolean isSynchronus)
  {
    this.isSynchronus = isSynchronus;
  }
  
  @Override
  public Boolean getIsExtracted()
  {
    return isExtracted;
  }
  
  @Override
  public void setIsExtracted(Boolean isExtracted)
  {
    this.isExtracted = isExtracted;
  }

  @Override
  public String getCode()
  {
    return code;
  }

  @Override
  public void setCode(String code)
  {
    this.code = code;
  }

  @Override
  public String getThumbnailPath()
  {
    return thumbnailPath;
  }

  @Override
  public void setThumbnailPath(String thumbnailPath)
  {
    this.thumbnailPath = thumbnailPath;
  }

  @Override
  public boolean isInDesignServerEnabled()
  {
    return isInDesignServerEnabled;
  }

  @Override
  public void setInDesignServerEnabled(boolean isInDesignServerEnabled)
  {
    this.isInDesignServerEnabled = isInDesignServerEnabled;
  }
  
  
}
