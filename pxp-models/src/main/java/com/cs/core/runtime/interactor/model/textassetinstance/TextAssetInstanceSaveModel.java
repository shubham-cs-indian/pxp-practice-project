package com.cs.core.runtime.interactor.model.textassetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.textassetinstance.ITextAssetInstance;
import com.cs.core.runtime.interactor.entity.textassetinstance.TextAssetInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceSaveModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class TextAssetInstanceSaveModel extends AbstractContentInstanceSaveModel
    implements ITextAssetInstanceSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceSaveModel()
  {
    this.entity = new TextAssetInstance();
  }
  
  public TextAssetInstanceSaveModel(ITextAssetInstance textassetInstance)
  {
    this.entity = textassetInstance;
  }
  
  @Override
  public String getBranchOf()
  {
    return ((ITextAssetInstance) entity).getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    ((ITextAssetInstance) entity).setBranchOf(branchOf);
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    // TODO Auto-generated method stub
    
  }
}
