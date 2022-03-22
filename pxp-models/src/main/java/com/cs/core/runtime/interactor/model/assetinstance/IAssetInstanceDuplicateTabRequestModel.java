package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;

public interface IAssetInstanceDuplicateTabRequestModel extends IContentTypeIdsInfoModel {
  
  public static final String FROM = "from";
  public static final String SIZE = "size";
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
}
