package com.cs.core.runtime.interactor.model.taskinstance;

import java.io.Serializable;
import java.util.HashMap;

public interface IAssetInfoModel extends Serializable {
  
  public static final String THUMB_KEY        = "thumbKey";
  public static final String FILENAME         = "filename";
  public static final String TYPE             = "type";
  public static final String PROPERTIES       = "properties";
  public static final String ASSET_OBJECT_KEY = "assetObjectKey";
  public static final String PREVIEW_KEY      = "previewKey";
  
  public HashMap<String, String> getProperties();
  
  public void setProperties(HashMap<String, String> properties);
  
  public String getThumbKey();
  
  public void setThumbKey(String thumbKey);
  
  public String getFilename();
  
  public void setFilename(String filename);
  
  public String getType();
  
  public void setType(String type);
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String assetObjectKey);
  
  public String getPreviewKey();
  
  public void setPreviewKey(String previewKey);
}
