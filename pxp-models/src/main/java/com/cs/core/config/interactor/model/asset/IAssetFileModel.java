package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IAssetFileModel extends IIdsListParameterModel {
  
  public static final String CONTAINER                   = "container";
  public static final String BYTES                       = "bytes";
  public static final String NAME                        = "name";
  public static final String EXTENSION                   = "extension";
  public static final String PATH                        = "path";
  public static final String KEY                         = "key";
  public static final String ASSET_OJECT_KEY             = "assetObjectKey";
  public static final String SHOULD_CHECK_FOR_REDUNDANCY = "shouldCheckForRedundancy";
  public static final String THUMB_KEY                   = "thumbKey";
  public static final String EXTENSION_CONFIGURATION     = "extensionConfiguration";
  public static final String EXTENSION_TYPE              = "extensionType";
  public static final String KLASS_ID                    = "klassId";
  public static final String IS_SYNCHRONUS               = "isSynchronus";
  public static final String IS_EXTRACTED                = "isExtracted";
  public static final String CODE                        = "code";
  public static final String THUMBNAIL_PATH              = "thumbnailPath";
  public static final String IS_INDESIGN_SERVER_ENABLED  = "isInDesignServerEnabled";  
  
  public String getContainer();
  
  public void setContainer(String container);
  
  public byte[] getBytes();
  
  public void setBytes(byte[] bytes);
  
  public String getName();
  
  public void setName(String name);
  
  public String getExtension();
  
  public void setExtension(String extension);
  
  public String getPath();
  
  public void setPath(String path);
  
  public String getKey();
  
  public void setKey(String key);
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String assetObjectKey);
  
  public String getThumbKey();
  
  public void setThumbKey(String thumbKey);
  
  public Boolean getShouldCheckForRedundancy();
  
  public void setShouldCheckForRedundancy(Boolean shouldCheckForRedundancy);
  
  public IAssetExtensionConfiguration getExtensionConfiguration();
  
  public void setExtensionConfiguration(IAssetExtensionConfiguration extensionConfiguration);
  
  public String getExtensionType();
  
  public void setExtensionType(String extensionType);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public Boolean getIsSynchronus();
  
  public void setIsSynchronus(Boolean isSynchronus);
  
  public Boolean getIsExtracted();
  
  public void setIsExtracted(Boolean isExtracted);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getThumbnailPath();
  
  public void setThumbnailPath(String thumbnailPath);
  
  public boolean isInDesignServerEnabled();
  
  public void setInDesignServerEnabled(boolean isInDesignServerEnabled);
}
