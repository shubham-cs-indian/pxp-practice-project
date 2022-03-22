package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.List;


public class AssetUploadSuccessResponseModel implements IAssetUploadSuccessResponseModel {
  
  private static final long     serialVersionUID   = 1L;
  private List<IAssetKeysModel> assetKeysModelList = new ArrayList<>();
  private long                  duplicateId        = 0;
  private List<Long>            successIIds        = new ArrayList<>();
  
  @Override
  public List<IAssetKeysModel> getAssetKeysModelList()
  {
    return assetKeysModelList;
  }
  
  @Override
  public void setAssetKeysModelList(List<IAssetKeysModel> assetKeysModelList)
  {
    this.assetKeysModelList = assetKeysModelList;
  }
  
  @Override
  public long getDuplicateId()
  {
    return duplicateId;
  }
  
  @Override
  public void setDuplicateId(long duplicateId)
  {
    this.duplicateId = duplicateId;
  }

  @Override
  public List<Long> getSuccessIIds()
  {
    return successIIds;
  }

  @Override
  public void setSuccessIIds(List<Long> successIIds)
  {
    this.successIIds = successIIds;
  }
  
  
}
