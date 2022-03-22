package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.asset.IdLabelCodeDownloadPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class AssetShareDialogInformationModel implements IAssetShareDialogInformationModel {
  
  private static final long                           serialVersionUID            = 1L;
  private List<String>                                masterAssetIdsList          = new ArrayList<>();
  protected List<IIdLabelCodeDownloadPermissionModel> technicalVariantTypeIdsList = new ArrayList<>();
  protected List<IIdLabelCodeDownloadPermissionModel> masterAssetTypeIdsList      = new ArrayList<>();
  
  @Override
  public List<IIdLabelCodeDownloadPermissionModel> getTechnicalVariantTypeIdsList()
  {
    return technicalVariantTypeIdsList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeDownloadPermissionModel.class)
  public void setTechnicalVariantTypeIdsList(List<IIdLabelCodeDownloadPermissionModel> technicalVariantTypeIdsList)
  {
    this.technicalVariantTypeIdsList = technicalVariantTypeIdsList;
  }
  
  @Override
  public List<IIdLabelCodeDownloadPermissionModel> getMasterAssetTypeIdsList()
  {
    return masterAssetTypeIdsList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeDownloadPermissionModel.class)
  public void setMasterAssetTypeIdsList(List<IIdLabelCodeDownloadPermissionModel> masterAssetTypeIdsList)
  {
    this.masterAssetTypeIdsList = masterAssetTypeIdsList;
  }

  @Override
  public List<String> getMasterAssetIdsList()
  {
    return masterAssetIdsList;
  }

  @Override
  public void setMasterAssetIdsList(List<String> masterAssetIdsList)
  {
    this.masterAssetIdsList = masterAssetIdsList;
  }
  
}
