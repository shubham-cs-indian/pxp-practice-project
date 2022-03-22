package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.entity.klass.AssetCollectionKlass;
import com.cs.core.config.interactor.entity.klass.IAsset;

public class AssetCollectionKlassSaveModel extends AssetKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public AssetCollectionKlassSaveModel()
  {
    super(new AssetCollectionKlass());
  }
  
  public AssetCollectionKlassSaveModel(IAsset klass)
  {
    super(klass);
  }
}
