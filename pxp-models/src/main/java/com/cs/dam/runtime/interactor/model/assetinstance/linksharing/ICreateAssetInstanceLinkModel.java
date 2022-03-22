package com.cs.dam.runtime.interactor.model.assetinstance.linksharing;

import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface ICreateAssetInstanceLinkModel extends IModel {
  
  public static final String ASSET_INSTANCE_ID     = "assetInstanceId";
  public static final String RENDITION_INSTANCE_ID = "renditionInstanceId";
  public static final String COVERFLOW_OBJECT      = "coverflowObject";
  
  public String getAssetInstanceId();
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getRenditionInstanceId();
  public void setRenditionInstanceId(String renditionInstanceId);
  
  public ICoverFlowModel getCoverflowObject();
  public void setCoverflowObject(ICoverFlowModel coverflowObject);
  
}
