package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetAssetExtensionsModel extends IModel {
  
  public static final String ASSET_EXTENSIONS = "assetExtensions";
  
  public Map<String, List<String>> getAssetExtensions();
  
  public void setAssetExtensions(Map<String, List<String>> assetExtensions);
}
