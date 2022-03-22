package com.cs.core.runtime.interactor.model.assetinstance;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBulkDownloadConfigInformationResponseModel extends IModel {
  
  public static final String MASTER_ASSET_KLASS_INFORMATION               = "masterAssetKlassInformation";
  public static final String TIV_ASSET_KLASS_INFORMATION                  = "tivAssetKlassInformation";
  public static final String MASTER_ASSET_TIV_KLASS_MAP                   = "masterAssetTivKlassMap";
  public static final String SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME = "shouldDownloadAssetWithOriginalFilename";
  
  public Map<String, IIdLabelCodeDownloadPermissionModel> getMasterAssetKlassInformation();
  public void setMasterAssetKlassInformation(Map<String, IIdLabelCodeDownloadPermissionModel> masterAssetKlassInformation);
  
  public Map<String, IIdLabelCodeDownloadPermissionModel> getTivAssetKlassInformation();
  public void setTivAssetKlassInformation(Map<String, IIdLabelCodeDownloadPermissionModel> tivAssetKlassInformation);
  
  public Map<String, List<String>> getMasterAssetTivKlassMap();
  public void setMasterAssetTivKlassMap(Map<String, List<String>> masterAssetTivKlassMap);
  
  public boolean getShouldDownloadAssetWithOriginalFilename();
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename);
}
