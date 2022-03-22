package com.cs.core.runtime.interactor.model.assetinstance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetAttributeInstanceInformationModel
    implements IAssetAttributeInstanceInformationModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected String                  thumbKey;
  
  // protected String assetObjectKey;
  
  protected HashMap<String, String> properties;
  
  // protected String previewImageKey;
  
  // ID properties
  // protected String id;
  
  protected String                  assetInstanceId;
  
  // CoverFlow instance properties
  protected Boolean                 isDefault        = false;
  
  protected String                  type;
  
  protected String                  label;
  
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
  
  /*@Override
  public String getAssetObjectKey()
  {
    return assetObjectKey;
  }
  
  @Override
  public void setAssetObjectKey(String assetObjectKey)
  {
    this.assetObjectKey = assetObjectKey;
  }*/
  
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
  
  /*@Override
  public String getPreviewImageKey()
  {
    return previewImageKey;
  }
  
  @Override
  public void setPreviewImageKey(String previewImageKey)
  {
    this.previewImageKey = previewImageKey;
  }*/
  
  @Override
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
}
