package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateImageVariantsModel extends CreateVariantModel
    implements ICreateImageVariantsModel {

  private static final long        serialVersionUID = 1L;
  protected IAssetInformationModel assetInformation;
  protected String                 thumbnailPath;
  
  @Override
  public IAssetInformationModel getAssetInformation()
  {
    return assetInformation;
  }

  @Override
  @JsonDeserialize(as=AssetInformationModel.class)
  public void setAssetInformation(IAssetInformationModel assetInformation)
  {
    this.assetInformation = assetInformation;
  }

  @Override
  public String getThumbnailPath()
  {
    return thumbnailPath;
  }

  @Override
  public void setThumbnailPath(String thumbnailPath)
  {
    this.thumbnailPath = thumbnailPath;
  }
}
