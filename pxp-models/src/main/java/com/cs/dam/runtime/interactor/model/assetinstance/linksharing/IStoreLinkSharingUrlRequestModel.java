package com.cs.dam.runtime.interactor.model.assetinstance.linksharing;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IStoreLinkSharingUrlRequestModel extends IModel {
  
  public static final String ASSET_INSTANCE_ID     = "assetInstanceId";
  public static final String RENDITION_INSTANCE_ID = "renditionInstanceId";
  public static final String SHARED_OBJECT_ID      = "sharedObjectId";
  public static final String SHARED_TIMESTAMP      = "sharedTimestamp";
  
  public String getAssetInstanceId();
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getRenditionInstanceId();
  public void setRenditionInstanceId(String renditionInstanceId);
  
  public String getSharedObjectId();
  public void setSharedObjectId(String sharedObjectId);
  
  public Long getSharedTimestamp();
  public void setSharedTimestamp(Long sharedTimestamp);
}
