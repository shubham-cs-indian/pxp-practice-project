package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;

import java.util.Map;

public interface ISaveStrategyContentInstanceResponseModel
    extends ISaveStrategyInstanceResponseModel {
  
  public static final String REFERENCED_ASSETS = "referencedAssets";
  
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets();
  
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets);
}
