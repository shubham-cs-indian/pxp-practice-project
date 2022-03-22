package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkAssetDownloadRequestModel extends IModel {
  
  public static String TECHNICAL_VARIANT_TYPE_IDS   = "technicalVariantTypeIds";
  public static String MASTER_ASSET_IDS             = "masterAssetIds";
  public static String MASTER_ASSET_PERMISSION_INFO = "masterAssetPermissionInfo";
  public static String TECHNICAL_VARIANT_DOWNLOAD   = "technicalVariantDownload";
  public static String MASTER_ASSET_DOWNLOAD        = "masterAssetDownload";
  public static String FOLDER_BY_ASSET              = "folderByAsset";
  public static String FOLDER_BY_TYPE               = "folderByType";
  public static String DOWNLOAD_FILENAME            = "downloadFileName";
  public static String BASE_TYPE                    = "baseType";
  public static String FOLDER_BY_ID                 = "folderById";
  public static String COMMENTS                     = "comments";
  
  public List<String> getTechnicalVariantTypeIds();
  
  public void setTechnicalVariantTypeIds(List<String> technicalVariantTypeIds);
  
  public List<String> getMasterAssetIds();
  
  public void setMasterAssetIds(List<String> masterAssetIds);
  
  public List<IIdLabelCodeDownloadPermissionModel> getMasterAssetPermissionInfo();
  
  public void setMasterAssetPermissionInfo(
      List<IIdLabelCodeDownloadPermissionModel> masterAssetPermissionInfo);
  
  public Boolean getTechnicalVariantDownload();
  
  public void setTechnicalVariantDownload(Boolean technicalVariantDownload);
  
  public Boolean getMasterAssetDownload();
  
  public void setMasterAssetDownload(Boolean masterAssetDownload);
  
  public Boolean getFolderByAsset();
  
  public void setFolderByAsset(Boolean folderByAsset);
  
  public Boolean getFolderByType();
  
  public void setFolderByType(Boolean folderByType);
  
  public String getDownloadFileName();
  
  public void setDownloadFileName(String downloadFileName);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Boolean getFolderById();
  
  public void setFolderById(Boolean setFolderById);
  
  public String getComments();
  public void setComments(String comments);
}
