package com.cs.core.runtime.interactor.model.textassetinstance;

import com.cs.core.config.interactor.entity.textasset.ITextAsset;

public class SaveTextAssetInstanceStrategyModel implements ISaveTextAssetInstanceStrategyModel {
  
  protected ITextAssetInstanceSaveModel klassInstance;
  protected ITextAsset                  typeKlass;
  
  @Override
  public ITextAssetInstanceSaveModel getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(ITextAssetInstanceSaveModel klassInstance)
  {
    this.klassInstance = (ITextAssetInstanceSaveModel) klassInstance;
  }
  
  @Override
  public ITextAsset getTypeKlass()
  {
    return this.typeKlass;
  }
  
  @Override
  public void setTypeKlass(ITextAsset typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
