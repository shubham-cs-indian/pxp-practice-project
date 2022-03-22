package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.entity.klassinstance.IMarketInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.MarketInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceSaveModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class MarketInstanceSaveModel extends AbstractContentInstanceSaveModel
    implements IMarketInstanceSaveModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceSaveModel()
  {
    this.entity = new MarketInstance();
  }
  
  public MarketInstanceSaveModel(IMarketInstance marketInstance)
  {
    this.entity = marketInstance;
  }
  
  @Override
  public String getBranchOf()
  {
    return ((IMarketInstance) entity).getBranchOf();
  }
  
  @Override
  public void setBranchOf(String branchOf)
  {
    ((IMarketInstance) entity).setBranchOf(branchOf);
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
