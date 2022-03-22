package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceImage;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceImage;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class AssetConfirmAddDeleteModel implements IAssetConfirmAddDeleteModel {
  
  protected List<? extends IKlassInstanceImage> addedAsset   = new ArrayList<>();
  
  protected List<? extends IKlassInstanceImage> deletedAsset = new ArrayList<>();
  
  @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public List<? extends IKlassInstanceImage> getAddedAssets()
  {
    return addedAsset;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public void setAddedAssets(List<? extends IKlassInstanceImage> addedImages)
  {
    this.addedAsset = addedImages;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public List<? extends IKlassInstanceImage> getDeletedAssets()
  {
    return deletedAsset;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceImage.class)
  @Override
  public void setDeletedAssets(List<? extends IKlassInstanceImage> deletedImages)
  {
    this.deletedAsset = deletedImages;
  }
}
