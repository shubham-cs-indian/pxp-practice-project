package com.cs.core.runtime.interactor.model.textassetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.textasset.TextAsset;
import com.cs.core.runtime.interactor.entity.textassetinstance.ITextAssetInstance;
import com.cs.core.runtime.interactor.entity.textassetinstance.TextAssetInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class TextAssetInstanceModel extends AbstractContentInstanceModel
    implements ITextAssetInstanceModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  protected IKlass          typeKlass;
  
  public TextAssetInstanceModel()
  {
    this.entity = new TextAssetInstance();
  }
  
  public TextAssetInstanceModel(ITextAssetInstance textassetInstance)
  {
    this.entity = textassetInstance;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @JsonDeserialize(as = TextAsset.class)
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
