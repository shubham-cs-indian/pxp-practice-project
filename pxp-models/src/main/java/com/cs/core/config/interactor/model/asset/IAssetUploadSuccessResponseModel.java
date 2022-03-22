package com.cs.core.config.interactor.model.asset;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IAssetUploadSuccessResponseModel extends IModel {
  
  public static final String ASSET_KEYS_MODEL_LIST = "assetKeysModelList";
  public static final String DUPLICATE_ID          = "duplicateId";
  public static final String SUCCESS_IIDS          = "successIIds";
  
  public List<IAssetKeysModel> getAssetKeysModelList();
  public void setAssetKeysModelList(List<IAssetKeysModel> assetKeysModel);
  
  public long getDuplicateId();
  public void setDuplicateId(long duplicateId);
  
  public List<Long> getSuccessIIds();
  public void setSuccessIIds(List<Long> successIIds);
}
