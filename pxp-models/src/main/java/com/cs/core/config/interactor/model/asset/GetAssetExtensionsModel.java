package com.cs.core.config.interactor.model.asset;

import java.util.List;
import java.util.Map;

public class GetAssetExtensionsModel implements IGetAssetExtensionsModel {
  
  private static final long           serialVersionUID = 1L;
  
  protected Map<String, List<String>> assetExtensions;
  
  @Override
  public Map<String, List<String>> getAssetExtensions()
  {
    return this.assetExtensions;
  }
  
  @Override
  public void setAssetExtensions(Map<String, List<String>> assetExtensions)
  {
    this.assetExtensions = assetExtensions;
  }
}
