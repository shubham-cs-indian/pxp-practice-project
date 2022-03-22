package com.cs.dam.runtime.interactor.model.assetinstance.linksharing;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IBulkCreateAssetInstanceLinkRequestModel extends IModel {
  
  public static String MASTER_ASSET_IDS           = "masterAssetIds";
  public static String TECHNICAL_VARIANT_TYPE_IDS = "technicalVariantTypeIds";
  public static String MASTER_ASSET_SHARE         = "masterAssetShare";
  
  public List<String> getMasterAssetIds();
  public void setMasterAssetIds(List<String> masterAssetIds);
  
  public List<String> getTechnicalVariantTypeIds();
  public void setTechnicalVariantTypeIds(List<String> technicalVariantTypeIds);
  
  public Boolean getMasterAssetShare();
  public void setMasterAssetShare(Boolean masterAssetShare);
}
