package com.cs.core.runtime.interactor.entity.propertyinstance;

import java.util.HashMap;

public interface IImageAttributeInstance extends IContentAttributeInstance {
  
  public static final String THUMB_KEY         = "thumbKey";
  public static final String ASSET_OBJECT_KEY  = "assetObjectKey";
  public static final String PROPERTIES        = "properties";
  public static final String BYTESTREAM        = "ByteStream";
  public static final String FILENAME          = "fileName";
  public static final String IS_DEFAULT        = "isDefault";
  public static final String DESCRIPTION       = "description";
  public static final String TYPE              = "type";
  public static final String PREVIEW_IMAGE_KEY = "previewImageKey";
  public static final String HASH              = "hash";
  
  public String getThumbKey();
  
  public void setThumbKey(String imageKey);
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String type);
  
  public HashMap<String, String> getProperties();
  
  public void setProperties(HashMap<String, String> properties);
  
  public String getByteStream();
  
  public void setByteStream(String byteStream);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public Boolean getIsDefault();
  
  public void setIsDefault(Boolean isDefault);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getType();
  
  public void setType(String type);
  
  public String getPreviewImageKey();
  
  public void setPreviewImageKey(String previewImageKey);
  
  public String getHash();
  
  public void setHash(String hash);
}
