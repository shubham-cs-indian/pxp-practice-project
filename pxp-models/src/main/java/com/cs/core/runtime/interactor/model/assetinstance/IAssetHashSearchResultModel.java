package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetHashSearchResultModel extends IModel {
  
  public static final String SEARCH_RESULT = "searchResult";
  
  public Boolean getSearchResult();
  
  public void setSearchResult(Boolean result);
}
