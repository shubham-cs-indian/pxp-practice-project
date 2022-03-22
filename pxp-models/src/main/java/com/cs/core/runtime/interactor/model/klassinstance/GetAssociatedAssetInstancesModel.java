package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetAssociatedAssetInstancesModel implements IGetAssociatedAssetInstancesModel {
  
  private static final long                               serialVersionUID = 1L;
  
  protected List<IAssetAttributeInstanceInformationModel> referencedAssets = new ArrayList<>();
  
  @Override
  public List<IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @Override
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  public void setReferencedAssets(List<IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
}
