package com.cs.core.config.interactor.model.assetstatus;

public class AssetUploadStatusCheckModel implements IAssetUploadStatusCheckModel {
  
  protected String container;
  protected String key;
  
  public AssetUploadStatusCheckModel(String container, String key)
  {
    this.container = container;
    this.key = key;
  }
  
  @Override
  public String getContainer()
  {
    return container;
  }
  
  @Override
  public void setContainer(String container)
  {
    this.container = container;
  }
  
  @Override
  public String getKey()
  {
    return key;
  }
  
  @Override
  public void setKey(String key)
  {
    this.key = key;
  }
}
