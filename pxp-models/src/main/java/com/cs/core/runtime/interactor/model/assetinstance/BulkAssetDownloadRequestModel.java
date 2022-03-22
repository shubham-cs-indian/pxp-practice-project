package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.model.asset.IdLabelCodeDownloadPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkAssetDownloadRequestModel implements IBulkAssetDownloadRequestModel {
  
  private static final long                           serialVersionUID          = 1L;
  protected List<String>                              technicalVariantTypeIds   = new ArrayList<>();
  protected List<String>                              masterAssetIds            = new ArrayList<>();
  protected List<IIdLabelCodeDownloadPermissionModel> masterAssetPermissionInfo = new ArrayList<>();
  protected Boolean                                   masterAssetDownload;
  protected Boolean                                   technicalVariantDownload;
  protected Boolean                                   folderByAsset             = false;
  protected Boolean                                   folderByType              = false;
  protected Boolean                                   folderById                = false;
  protected String                                    downloadFileName;
  protected String                                    baseType;
  protected String                                    comments;
  
  @Override
  public List<String> getTechnicalVariantTypeIds()
  {
    return technicalVariantTypeIds;
  }
  
  @Override
  public void setTechnicalVariantTypeIds(List<String> technicalVariantTypeIds)
  {
    this.technicalVariantTypeIds = technicalVariantTypeIds;
  }
  
  @Override
  public List<String> getMasterAssetIds()
  {
    return masterAssetIds;
  }
  
  @Override
  public void setMasterAssetIds(List<String> masterAssetIds)
  {
    this.masterAssetIds = masterAssetIds;
  }
  
  @Override
  public Boolean getMasterAssetDownload()
  {
    return masterAssetDownload;
  }
  
  @Override
  public void setMasterAssetDownload(Boolean masterAssetDownload)
  {
    this.masterAssetDownload = masterAssetDownload;
  }
  
  @Override
  public Boolean getTechnicalVariantDownload()
  {
    return technicalVariantDownload;
  }
  
  @Override
  public void setTechnicalVariantDownload(Boolean technicalVariantDownload)
  {
    this.technicalVariantDownload = technicalVariantDownload;
  }
  
  @Override
  public Boolean getFolderByAsset()
  {
    return folderByAsset;
  }
  
  @Override
  public void setFolderByAsset(Boolean folderByAsset)
  {
    this.folderByAsset = folderByAsset;
  }
  
  @Override
  public Boolean getFolderByType()
  {
    return folderByType;
  }
  
  @Override
  public void setFolderByType(Boolean folderByType)
  {
    this.folderByType = folderByType;
  }
  
  @Override
  public List<IIdLabelCodeDownloadPermissionModel> getMasterAssetPermissionInfo()
  {
    return masterAssetPermissionInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeDownloadPermissionModel.class)
  public void setMasterAssetPermissionInfo(
      List<IIdLabelCodeDownloadPermissionModel> masterAssetPermissionInfo)
  {
    this.masterAssetPermissionInfo = masterAssetPermissionInfo;
  }
  
  @Override
  public String getDownloadFileName()
  {
    return downloadFileName;
  }
  
  @Override
  public void setDownloadFileName(String downloadFileName)
  {
    this.downloadFileName = downloadFileName;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Boolean getFolderById()
  {
    return folderById;
  }
  
  @Override
  public void setFolderById(Boolean folderById)
  {
    this.folderById = folderById;
  }
  
  @Override
  public String getComments()
  {
    return comments;
  }

  @Override
  public void setComments(String comments)
  {
    this.comments = comments;
  }
}
