package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAssetInstanceModel extends IModel {
  
  public static final String TYPE              = "type";
  public static final String KLASS_INSTANCES   = "klassInstances";
  public static final String GLOBAL_PERMISSION = "globalPermission";
  
  public IAssetInstance getKlassInstance();
  
  public void setKlassInstance(IAssetInstance assetInstance);
  
  public IAsset getType();
  
  public void setType(IAsset asset);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
}
