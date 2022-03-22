package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;

public class CoverFlowModel implements ICoverFlowModel {
  
  private static final long serialVersionUID = 1L;
  protected String          assetObjectKey;
  protected String          fileName;
  protected String          type;
  
  public CoverFlowModel(String assetObjectKey, String fileName, String type)
  {
    super();
    this.assetObjectKey = assetObjectKey;
    this.fileName = fileName;
    this.type = type;
  }
  
  public CoverFlowModel()
  {
  }
  
  @Override
  public String getAssetObjectKey()
  {
    return assetObjectKey;
  }
  
  @Override
  public void setAssetObjectKey(String assetObjectKey)
  {
    this.assetObjectKey = assetObjectKey;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
