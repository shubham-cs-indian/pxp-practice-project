package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetHashSearchResultModel;

public class AssetHashSearchResultModel implements IAssetHashSearchResultModel {
  
  protected Boolean result = false;
  
  @Override
  public Boolean getSearchResult()
  {
    return result;
  }
  
  @Override
  public void setSearchResult(Boolean result)
  {
    this.result = result;
  }
}
