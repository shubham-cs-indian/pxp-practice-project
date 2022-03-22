package com.cs.core.runtime.interactor.model.textassetinstance;

import com.cs.core.config.interactor.entity.textasset.ITextAsset;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveTextAssetInstanceStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE = "klassInstance";
  public static final String TYPE_KLASS     = "typeKlass";
  
  public ITextAssetInstanceSaveModel getKlassInstance();
  
  public void setKlassInstance(ITextAssetInstanceSaveModel klassInstance);
  
  public ITextAsset getTypeKlass();
  
  public void setTypeKlass(ITextAsset typeKlass);
}
