package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;

public class AssetInstanceDuplicateTabRequestModel extends ContentTypeIdsInfoModel
    implements IAssetInstanceDuplicateTabRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Integer         from;
  protected Integer         size;
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
}
