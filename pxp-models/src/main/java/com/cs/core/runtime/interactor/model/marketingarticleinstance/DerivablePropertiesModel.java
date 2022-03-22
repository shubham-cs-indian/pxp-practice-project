package com.cs.core.runtime.interactor.model.marketingarticleinstance;

import java.util.ArrayList;
import java.util.List;

public class DerivablePropertiesModel implements IDerivablePropertiesModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    derivableAttributeIds;
  protected List<String>    derivableTagIds;
  
  @Override
  public List<String> getDerivableAttributeIds()
  {
    if (derivableAttributeIds == null) {
      derivableAttributeIds = new ArrayList<>();
    }
    return derivableAttributeIds;
  }
  
  @Override
  public void setDerivableAttributeIds(List<String> derivableAttributeIds)
  {
    this.derivableAttributeIds = derivableAttributeIds;
  }
  
  @Override
  public List<String> getDerivableTagIds()
  {
    if (derivableTagIds == null) {
      derivableTagIds = new ArrayList<>();
    }
    return derivableTagIds;
  }
  
  @Override
  public void setDerivableTagIds(List<String> derivableTagIds)
  {
    this.derivableTagIds = derivableTagIds;
  }
}
