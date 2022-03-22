package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.asset.IMasterAssetDownloadInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkAssetDownloadStrategyResponseModel extends IModel {
  
  public static String TECHNICAL_VARIANT_INFO_LIST             = "technicalVariantInfoList";
  public static String MASTER_ASSET_COVERFLOW_LIST             = "masterAssetCoverflowList";
  public static String ASSET_WITH_DOWNLOAD_PERMISSION_EXIST    = "assetWithDownloadPermissionExist";
  public static String ASSET_WITHOUT_DOWNLOAD_PERMISSION_EXIST = "assetWithoutDownloadPermissionExist";
  
  public List<IRenditionDownloadInfoModel> getTechnicalVariantInfoList();
  
  public void setTechnicalVariantInfoList(
      List<IRenditionDownloadInfoModel> technicalVariantInfoList);
  
  public List<IMasterAssetDownloadInfoModel> getMasterAssetCoverflowList();
  
  public void setMasterAssetCoverflowList(
      List<IMasterAssetDownloadInfoModel> masterAssetCoverflowList);
  
  public boolean getAssetWithDownloadPermissionExist();
  
  public void setAssetWithDownloadPermissionExist(boolean assetWithDownloadPermissionExist);
  
  public boolean getAssetWithoutDownloadPermissionExist();
  public void setAssetWithoutDownloadPermissionExist(boolean assetWithoutDownloadPermissionExist);
}
