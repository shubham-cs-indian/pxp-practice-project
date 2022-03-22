package com.cs.dam.runtime.interactor.model.assetinstance.linksharing;


public class StoreLinkSharingUrlRequestModel implements IStoreLinkSharingUrlRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          assetInstanceId;
  protected String          renditionInstanceId;
  protected String          sharedObjectId;
  protected Long            sharedTimestamp;
  
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
  
  @Override
  public String getRenditionInstanceId()
  {
    return renditionInstanceId;
  }
  
  @Override
  public void setRenditionInstanceId(String renditionInstanceId)
  {
    this.renditionInstanceId = renditionInstanceId;
  }
  
  @Override
  public String getSharedObjectId()
  {
    return sharedObjectId;
  }
  
  @Override
  public void setSharedObjectId(String sharedObjectId)
  {
    this.sharedObjectId = sharedObjectId;
  }

  @Override
  public Long getSharedTimestamp()
  {
    return sharedTimestamp;
  }

  @Override
  public void setSharedTimestamp(Long sharedTimestamp)
  {
    this.sharedTimestamp = sharedTimestamp;
  }
}
