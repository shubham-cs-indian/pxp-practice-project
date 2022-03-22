package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceImage;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAssetConfirmAddDeleteModel extends IModel {
  
  public static final String ADDED_ASSETS   = "addedAssets";
  public static final String DELETED_ASSETS = "deletedAssets";
  
  public List<? extends IKlassInstanceImage> getAddedAssets();
  
  public void setAddedAssets(List<? extends IKlassInstanceImage> addedAssets);
  
  public List<? extends IKlassInstanceImage> getDeletedAssets();
  
  public void setDeletedAssets(List<? extends IKlassInstanceImage> deletedAssets);
}
