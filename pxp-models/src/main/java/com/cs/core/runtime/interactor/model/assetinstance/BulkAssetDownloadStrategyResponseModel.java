package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.asset.IMasterAssetDownloadInfoModel;
import com.cs.core.config.interactor.model.asset.MasterAssetDownloadInfoModel;
import com.cs.core.config.interactor.model.asset.RenditionDownloadInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkAssetDownloadStrategyResponseModel
    implements IBulkAssetDownloadStrategyResponseModel {
  
  private static final long                     serialVersionUID                    = 1L;
  protected List<IRenditionDownloadInfoModel>   technicalVariantInfoList            = new ArrayList<>();
  protected List<IMasterAssetDownloadInfoModel> masterAssetCoverflowList            = new ArrayList<>();
  protected boolean                             assetWithDownloadPermissionExist;
  protected boolean                             assetWithoutDownloadPermissionExist = false;
  
  @Override
  public List<IRenditionDownloadInfoModel> getTechnicalVariantInfoList()
  {
    return technicalVariantInfoList;
  }
  
  @JsonDeserialize(contentAs = RenditionDownloadInfoModel.class)
  @Override
  public void setTechnicalVariantInfoList(
      List<IRenditionDownloadInfoModel> technicalVariantInfoList)
  {
    this.technicalVariantInfoList = technicalVariantInfoList;
  }
  
  @Override
  public List<IMasterAssetDownloadInfoModel> getMasterAssetCoverflowList()
  {
    return masterAssetCoverflowList;
  }
  
  @JsonDeserialize(contentAs = MasterAssetDownloadInfoModel.class)
  @Override
  public void setMasterAssetCoverflowList(
      List<IMasterAssetDownloadInfoModel> masterAssetCoverflowList)
  {
    this.masterAssetCoverflowList = masterAssetCoverflowList;
  }
  
  @Override
  public boolean getAssetWithDownloadPermissionExist()
  {
    return assetWithDownloadPermissionExist;
  }
  
  @Override
  public void setAssetWithDownloadPermissionExist(boolean assetWithDownloadPermissionExist)
  {
    this.assetWithDownloadPermissionExist = assetWithDownloadPermissionExist;
  }

  @Override
  public boolean getAssetWithoutDownloadPermissionExist()
  {
    return assetWithoutDownloadPermissionExist;
  }

  @Override
  public void setAssetWithoutDownloadPermissionExist(boolean assetWithoutDownloadPermissionExist)
  {
    this.assetWithoutDownloadPermissionExist = assetWithoutDownloadPermissionExist;
  }
}
