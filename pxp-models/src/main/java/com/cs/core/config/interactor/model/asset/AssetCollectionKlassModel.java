package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.klass.AssetCollectionKlass;
import com.cs.core.config.interactor.entity.klass.IAsset;

public class AssetCollectionKlassModel extends AssetModel {
  
  private static final long serialVersionUID = 1L;
  
  public AssetCollectionKlassModel()
  {
    super(new AssetCollectionKlass());
  }
  
  public AssetCollectionKlassModel(IAsset klass)
  {
    super(klass);
  }
}
