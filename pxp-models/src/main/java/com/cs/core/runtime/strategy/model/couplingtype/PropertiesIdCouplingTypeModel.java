package com.cs.core.runtime.strategy.model.couplingtype;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PropertiesIdCouplingTypeModel implements IPropertiesIdCodeCouplingTypeModel {
  
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
  @JsonDeserialize(contentAs = IdCodeCouplingTypeModel.class)
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
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdCodeCouplingTypeModel.class)
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
  @JsonDeserialize(contentAs = IdCodeCouplingTypeModel.class)
  public void setDependentAttributes(List<IIdCodeCouplingTypeModel> dependentAttributes)
  {
    this.dependentAttributes = dependentAttributes;
  }
}
