package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRenditionDownloadInfoModel extends IModel {
  
  public static final String COVERFLOW_OBJECT           = "coverflowObject";
  public static final String RENDITION_ASSET_ID         = "renditionAssetId";
  public static final String RENDITION_CLASS_ID         = "renditionClassId";
  public static final String RENDITION_ASSET_MEDIA_NAME = "renditionAssetMediaName";
  public static final String PARENT_ASSET_INSTANCE_NAME = "parentAssetInstanceName";
  public static final String PARENT_ASSET_ID            = "parentAssetId";
  public static final String PARENT_CLASS_ID            = "parentClassId";
  public static final String PARENT_ASSET_MEDIA_NAME    = "parentAssetMediaName";
  
  public ICoverFlowModel getCoverflowObject();
  public void setCoverflowObject(ICoverFlowModel coverflowObject);
  
  public String getRenditionAssetId();
  public void setRenditionAssetId(String renditionAssetId);
  
  public String getRenditionClassId();
  public void setRenditionClassId(String renditionClassId);
  
  public String getRenditionAssetMediaName();
  public void setRenditionAssetMediaName(String renditionAssetMediaName);
  
  public String getParentAssetInstanceName();
  public void setParentAssetInstanceName(String parentAssetInstanceName);
  
  public String getParentAssetId();
  public void setParentAssetId(String parentAssetId);
  
  public String getParentClassId();
  public void setParentClassId(String parentClassId);
  
  public String getParentAssetMediaName();
  public void setParentAssetMediaName(String parentAssetMediaName);
}
