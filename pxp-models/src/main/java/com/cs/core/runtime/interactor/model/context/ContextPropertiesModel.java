package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;

import java.util.ArrayList;
import java.util.List;

public class ContextPropertiesModel implements IContextPropertiesModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<IIdCodeCouplingTypeModel> independentAttributes;
  protected List<IIdCodeCouplingTypeModel> tags;
  protected List<IIdCodeCouplingTypeModel> dependentAttributes;
  
  @Override
  public List<IIdCodeCouplingTypeModel> getIndependentAttributes()
  {
    if (independentAttributes == null) {
      independentAttributes = new ArrayList<>();
    }
    return independentAttributes;
  }
  
  @Override
  public void setIndependentAttributes(List<IIdCodeCouplingTypeModel> independentAttributes)
  {
    this.independentAttributes = independentAttributes;
  }
  
  @Override
  public List<IIdCodeCouplingTypeModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return null;
  }
  
  @Override
  public void setTags(List<IIdCodeCouplingTypeModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IIdCodeCouplingTypeModel> getDependentAttributes()
  {
    if (dependentAttributes == null) {
      dependentAttributes = new ArrayList<>();
    }
    return dependentAttributes;
  }
  
  @Override
  public void setDependentAttributes(List<IIdCodeCouplingTypeModel> dependentAttributes)
  {
    this.dependentAttributes = dependentAttributes;
  }
}
