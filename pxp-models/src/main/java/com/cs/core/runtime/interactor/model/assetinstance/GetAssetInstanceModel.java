package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAssetInstanceModel implements IGetAssetInstanceModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected AssetInstance     assetInstance;
  
  protected IAsset            asset;
  
  protected IGlobalPermission globalPermission;
  
  @Override
  public IAssetInstance getKlassInstance()
  {
    return this.assetInstance;
  }
  
  @Override
  public void setKlassInstance(IAssetInstance assetInstance)
  {
    this.assetInstance = (AssetInstance) assetInstance;
  }
  
  @Override
  public IAsset getType()
  {
    return asset;
  }
  
  @Override
  public void setType(IAsset asset)
  {
    this.asset = asset;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
}
