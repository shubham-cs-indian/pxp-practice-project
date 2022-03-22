package com.cs.dam.runtime.interactor.model.assetinstance.linksharing;

import com.cs.core.config.interactor.model.asset.CoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class CreateAssetInstanceLinkModel implements ICreateAssetInstanceLinkModel {
  
  private static final long serialVersionUID = 1L;
  protected String          assetInstanceId;
  protected String          renditionInstanceId;
  protected ICoverFlowModel coverflowObject;
  
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
  public ICoverFlowModel getCoverflowObject()
  {
    return coverflowObject;
  }
  
  @Override
  @JsonDeserialize(as=CoverFlowModel.class)
  public void setCoverflowObject(ICoverFlowModel coverflowObject)
  {
    this.coverflowObject = coverflowObject;
  }
}
