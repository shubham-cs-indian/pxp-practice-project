package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAssociatedAssetInstancesModel extends IModel {
  
  public static final String REFERENCED_ASSETS = "referencedAssets";
  
  public List<IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(List<IAssetAttributeInstanceInformationModel> referencedAssets);
}
