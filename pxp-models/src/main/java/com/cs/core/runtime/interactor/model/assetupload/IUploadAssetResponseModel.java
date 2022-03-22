package com.cs.core.runtime.interactor.model.assetupload;

import java.util.List;

import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IUploadAssetResponseModel extends IModel {
  
  public static final String ASSET_KEYS_MODEL_LIST = "assetKeysModelList";
  public static final String FILE_MODEL_LIST       = "fileKeysModelList";
  public static final String ASSET_CONFIG_MODEL    = "assetConfigModel";
  public static final String DUPLICATE_IID         = "duplicateIId";
  public static final String FAILURE               = "failure";
  
  public List<IAssetKeysModel> getAssetKeysModelList();
  public void setAssetKeysModelList(List<IAssetKeysModel> assetKeysModel);
  
  public List<IAssetFileModel> getFileModelList();
  public void setFileModelList(List<IAssetFileModel> fileModelList);
  
  public IAssetConfigurationDetailsResponseModel getAssetConfigModel();
  public void setAssetConfigModel(IAssetConfigurationDetailsResponseModel assetConfigModel);
  
  public long getDuplicateIId();
  public void setDuplicateIId(long duplicateIId);
  
  public IExceptionModel getFailure();
  public void setFailure(IExceptionModel failure);
}
