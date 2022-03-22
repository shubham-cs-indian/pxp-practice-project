package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.HashMap;

public interface IReferenceAssetModel extends IModel {
  
  public static final String THUMB_KEY         = "thumbKey";
  public static final String ASSET_OBJECT_KEY  = "assetObjectKey";
  public static final String PROPERTIES        = "properties";
  public static final String PREVIEW_IMAGE_KEY = "previewImageKey";
  public static final String FILE_NAME         = "fileName";
  public static final String DESCRIPTION       = "description";
  public static final String IS_DEFAULT        = "isDefault";
  public static final String TYPE              = "type";
  
  public String getThumbKey();
  
  public void setThumbKey(String thumbKey);
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String assetObjectKey);
  
  public HashMap<String, String> getProperties();
  
  public void setProperties(HashMap<String, String> properties);
  
  public String getType();
  
  public void setType(String type);
  
  public String getPreviewImageKey();
  
  public void setPreviewImageKey(String previewImageKey);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public Boolean getIsDefault();
  
  public void setIsDefault(Boolean isDefault);
}
