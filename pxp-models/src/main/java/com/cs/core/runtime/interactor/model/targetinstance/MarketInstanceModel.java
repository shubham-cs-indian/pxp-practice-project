package com.cs.core.runtime.interactor.model.targetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.Market;
import com.cs.core.runtime.interactor.entity.klassinstance.IMarketInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.MarketInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractContentInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class MarketInstanceModel extends AbstractContentInstanceModel
    implements IMarketInstanceModel {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  protected IKlass          typeKlass;
  
  public MarketInstanceModel()
  {
    this.entity = new MarketInstance();
  }
  
  public MarketInstanceModel(IMarketInstance marketInstance)
  {
    this.entity = marketInstance;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @JsonDeserialize(as = Market.class)
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
