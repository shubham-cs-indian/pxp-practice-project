package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

public interface IMasterAssetDownloadInfoModel extends ICoverFlowModel, IIdLabelModel {
  
  public static final String MASTER_ASSET_CLASS_ID   = "masterAssetClassId";
  public static final String MASTER_ASSET_MEDIA_NAME = "masterAssetMediaName";
  
  public String getMasterAssetClassId();
  public void setMasterAssetClassId(String masterAssetClassId);
  
  public String getMasterAssetMediaName();
  public void setMasterAssetMediaName(String masterAssetMediaName);
}
