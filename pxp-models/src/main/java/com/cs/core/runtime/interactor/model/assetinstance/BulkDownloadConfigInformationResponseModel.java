package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.asset.IdLabelCodeDownloadPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class BulkDownloadConfigInformationResponseModel implements IBulkDownloadConfigInformationResponseModel {
  
  private static final long                                serialVersionUID                        = 1L;
  private Map<String, IIdLabelCodeDownloadPermissionModel> masterAssetKlassInformation             = new HashMap<>();
  private Map<String, IIdLabelCodeDownloadPermissionModel> tivAssetKlassInformation                = new HashMap<>();
  private Map<String, List<String>>                        masterAssetTivKlassMap                  = new HashMap<>();
  private boolean                                          shouldDownloadAssetWithOriginalFilename = false;
  
  @Override
  public Map<String, IIdLabelCodeDownloadPermissionModel> getMasterAssetKlassInformation()
  {
    return masterAssetKlassInformation;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeDownloadPermissionModel.class)
  public void setMasterAssetKlassInformation(Map<String, IIdLabelCodeDownloadPermissionModel> masterAssetKlassInformation)
  {
    this.masterAssetKlassInformation = masterAssetKlassInformation;
  }
  
  @Override
  public Map<String, IIdLabelCodeDownloadPermissionModel> getTivAssetKlassInformation()
  {
    return tivAssetKlassInformation;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeDownloadPermissionModel.class)
  public void setTivAssetKlassInformation(Map<String, IIdLabelCodeDownloadPermissionModel> tivAssetKlassInformation)
  {
    this.tivAssetKlassInformation = tivAssetKlassInformation;
  }

  @Override
  public Map<String, List<String>> getMasterAssetTivKlassMap()
  {
    return masterAssetTivKlassMap;
  }

  @Override
  public void setMasterAssetTivKlassMap(Map<String, List<String>> masterAssetTivKlassMap)
  {
    this.masterAssetTivKlassMap = masterAssetTivKlassMap;
  }
  
  @Override
  public boolean getShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }
  
  @Override
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }
}
