package com.cs.core.runtime.interactor.model.dataintegration;

public interface IOutboundTileInfoModel extends ITileInfoModel {
  
  public static final String LABEL           = "label";
  public static final String ASSETINSTANCEID = "assetInstanceId";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getAssetInstanceId();
  
  public void setAssetInstanceId(String assetInstanceId);
}
