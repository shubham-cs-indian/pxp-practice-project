package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.HashMap;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetInformationModel extends IModel {
  
  public static final String THUMB_KEY         = "thumbKey";
  public static final String ASSET_OBJECT_KEY  = "assetObjectKey";
  public static final String PROPERTIES        = "properties";
  public static final String FILENAME          = "fileName";
  public static final String TYPE              = "type";
  public static final String PREVIEW_IMAGE_KEY = "previewImageKey";
  public static final String HASH              = "hash";
  public static final String LAST_MODIFIED     = "lastModified";
  public static final String FILE_PATH         ="filePath";
  public static final String THUMBNAIL_PATH    ="thumbnailPath";
  
  public String getThumbKey();
  
  public void setThumbKey(String imageKey);
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String type);
  
  public HashMap<String, String> getProperties();
  
  public void setProperties(HashMap<String, String> properties);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public String getType();
  
  public void setType(String type);
  
  public String getPreviewImageKey();
  
  public void setPreviewImageKey(String previewImageKey);
  
  public String getHash();
  
  public void setHash(String hash);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
  
  public String getFilePath();
  
  public void setFilePath(String filePath);
   
  public String getThumbnailPath();
  
  public void setThumbnailPath(String thumbnailPath);
}
