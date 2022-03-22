package com.cs.core.runtime.interactor.model.filter;

import java.util.ArrayList;
import java.util.List;

public class PropertyInstanceValueTypeFilterModer
    extends AbstractPropertyInstanceFilterModel<IFilterValueModel>
    implements IPropertyInstanceValueTypeFilterModel {
  
  private static final long serialVersionUID = 1L;

  @Override
  public List<IFilterValueModel> getMandatory()
  {
    if(mandatory == null) {
      mandatory = new ArrayList<>(); 
    }
    return this.mandatory;
  }
  
  @Override
  public void setMandatory(List<IFilterValueModel> values)
  {
    this.mandatory = values;
  }
  
  @Override
  public List<IFilterValueModel> getShould()
  {
    if(should == null) {
      should = new ArrayList<>();
    }
    return this.should;
  }
  
  @Override
  public void setShould(List<IFilterValueModel> should)
  {
    this.should = should;
  }
}
