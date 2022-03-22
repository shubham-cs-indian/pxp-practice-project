package com.cs.core.runtime.interactor.model.assetupload;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public class UploadAssetResponseModel implements IUploadAssetResponseModel {
  
  private static final long               serialVersionUID = 1L;
  protected List<IAssetKeysModel>         assetKeysModel   = new ArrayList<>();
  protected List<IAssetFileModel>         fileModelList    = new ArrayList<>();
  IAssetConfigurationDetailsResponseModel assetConfigModel;
  protected long                          duplicateIId     = 0;
  protected IExceptionModel               failure;
  
  @Override
  public List<IAssetKeysModel> getAssetKeysModelList()
  {
    return assetKeysModel;
  }
  
  @Override
  public void setAssetKeysModelList(List<IAssetKeysModel> assetKeysModel)
  {
    this.assetKeysModel = assetKeysModel;
  }
  
  @Override
  public List<IAssetFileModel> getFileModelList()
  {
    return fileModelList;
  }
  
  @Override
  public void setFileModelList(List<IAssetFileModel> fileModelList)
  {
    this.fileModelList = fileModelList;
  }
  
  @Override
  public IAssetConfigurationDetailsResponseModel getAssetConfigModel()
  {
    return assetConfigModel;
  }

  @Override
  public void setAssetConfigModel(IAssetConfigurationDetailsResponseModel assetConfigModel)
  {
    this.assetConfigModel = assetConfigModel;
  }
  
  @Override
  public long getDuplicateIId()
  {
    return duplicateIId;
  }
  
  @Override
  public void setDuplicateIId(long duplicateIId)
  {
    this.duplicateIId = duplicateIId;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
}
