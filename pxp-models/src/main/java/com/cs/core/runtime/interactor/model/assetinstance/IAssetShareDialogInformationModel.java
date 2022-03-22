package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetShareDialogInformationModel extends IModel {
  
  public static String TECHNICAL_VARIANT_TYPE_IDS_LIST         = "technicalVariantTypeIdsList";
  public static String MASTER_ASSET_TYPE_IDS_LIST              = "masterAssetTypeIdsList";
  public static String MASTER_ASSET_IDS_LIST                   = "masterAssetIdsList";
  
  public List<IIdLabelCodeDownloadPermissionModel> getTechnicalVariantTypeIdsList();
  public void setTechnicalVariantTypeIdsList(List<IIdLabelCodeDownloadPermissionModel> technicalVariantTypeIdsList);
  
  public List<IIdLabelCodeDownloadPermissionModel> getMasterAssetTypeIdsList();
  public void setMasterAssetTypeIdsList(List<IIdLabelCodeDownloadPermissionModel> masterAssetTypeIdsList);
  
  public List<String> getMasterAssetIdsList();
  public void setMasterAssetIdsList(List<String> masterAssetIdsList);
}
