package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IKlassInstanceImage extends IEntity {
  
  public static final String ASSET_OBJECT_KEY = "assetObjectKey";
  public static final String TYPE             = "type";
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String type);
  
  public String getType();
  
  public void setType(String type);
}
