package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.HashMap;

public interface IAssetAttributeInstanceInformationModel extends IModel {
  
  public static final String ASSET_INSTANCE_ID = "assetInstanceId";
  public static final String LABEL             = "label";
  public static final String THUMB_KEY         = "thumbKey";
  public static final String PROPERTIES        = "properties";
  public static final String TYPE              = "type";
  public static final String IS_DEFAULT        = "isDefault";
  
  public String getAssetInstanceId();
  
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getThumbKey();
  
  public void setThumbKey(String thumbKey);
  
  public HashMap<String, String> getProperties();
  
  public void setProperties(HashMap<String, String> properties);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsDefault();
  
  void setIsDefault(Boolean isDefault);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  /*
   * String getPreviewImageKey();
   *
   * void setPreviewImageKey(String previewImageKey);
   */
  /*
   * void setAssetObjectKey(String assetObjectKey);
   *
   * String getAssetObjectKey();
   */
  
}
