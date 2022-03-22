package com.cs.core.runtime.interactor.model.dataintegration;

public class OutboundTileInfoModel extends TileInfoModel implements IOutboundTileInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          label;
  protected String          assetInstanceId;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }
}
