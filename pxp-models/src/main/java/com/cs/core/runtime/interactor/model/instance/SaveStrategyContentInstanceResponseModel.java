package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;

import java.util.Map;

public class SaveStrategyContentInstanceResponseModel extends SaveStrategyInstanceResponseModel
    implements ISaveStrategyContentInstanceResponseModel {
  
  protected Map<String, ? extends IReferenceAssetModel> referencedAssets;
  
  @Override
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @Override
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
}
