package com.cs.core.runtime.interactor.model.filter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class PropertyInstanceRangeTypeFilterModel
    extends AbstractPropertyInstanceFilterModel<IFilterValueRangeModel>
    implements IPropertyInstanceRangeTypeFilterModel {
  
  @Override
  public List<IFilterValueRangeModel> getMandatory()
  {
    return this.mandatory;
  }
  
  @JsonDeserialize(contentAs = FilterValueRangeModel.class)
  @Override
  public void setMandatory(List<IFilterValueRangeModel> values)
  {
    this.mandatory = values;
  }
  
  @Override
  public List<IFilterValueRangeModel> getShould()
  {
    return this.should;
  }
  
  @Override
  public void setShould(List<IFilterValueRangeModel> should)
  {
    this.should = should;
  }
}
