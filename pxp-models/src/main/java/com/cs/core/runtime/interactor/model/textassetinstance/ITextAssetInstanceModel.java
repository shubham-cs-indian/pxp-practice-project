package com.cs.core.runtime.interactor.model.textassetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.textassetinstance.ITextAssetInstance;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;

public interface ITextAssetInstanceModel extends IRuntimeModel, ITextAssetInstance {
  
  public static final String TYPE_KLASS = "typeKlass";
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
}
