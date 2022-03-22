package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.IRenditionDownloadInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RenditionDownloadInfoModel implements IRenditionDownloadInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected ICoverFlowModel coverflowObject;
  protected String          renditionAssetId;
  protected String          renditionClassId;
  protected String          renditionAssetMediaName;
  protected String          parentAssetInstanceName;
  protected String          parentAssetId;
  protected String          parentClassId;
  protected String          parentAssetMediaName;
  
  @Override
  public ICoverFlowModel getCoverflowObject()
  {
    return coverflowObject;
  }
  
  @JsonDeserialize(as = CoverFlowModel.class)
  @Override
  public void setCoverflowObject(ICoverFlowModel coverflowObject)
  {
    this.coverflowObject = coverflowObject;
  }

  @Override
  public String getRenditionAssetId()
  {
    return renditionAssetId;
  }

  @Override
  public void setRenditionAssetId(String renditionAssetId)
  {
    this.renditionAssetId = renditionAssetId;
  }

  @Override
  public String getRenditionClassId()
  {
    return renditionClassId;
  }

  @Override
  public void setRenditionClassId(String renditionClassId)
  {
    this.renditionClassId = renditionClassId;
  }

  @Override
  public String getRenditionAssetMediaName()
  {
    return renditionAssetMediaName;
  }

  @Override
  public void setRenditionAssetMediaName(String renditionAssetMediaName)
  {
    this.renditionAssetMediaName = renditionAssetMediaName;
  }

  @Override
  public String getParentAssetInstanceName()
  {
    return parentAssetInstanceName;
  }

  @Override
  public void setParentAssetInstanceName(String parentAssetInstanceName)
  {
    this.parentAssetInstanceName = parentAssetInstanceName;
  }

  @Override
  public String getParentAssetId()
  {
    return parentAssetId;
  }

  @Override
  public void setParentAssetId(String parentAssetId)
  {
    this.parentAssetId = parentAssetId;
  }

  @Override
  public String getParentClassId()
  {
    return parentClassId;
  }

  @Override
  public void setParentClassId(String parentClassId)
  {
    this.parentClassId = parentClassId;
  }

  @Override
  public String getParentAssetMediaName()
  {
    return parentAssetMediaName;
  }

  @Override
  public void setParentAssetMediaName(String parentAssetMediaName)
  {
    this.parentAssetMediaName = parentAssetMediaName;
  }
}
