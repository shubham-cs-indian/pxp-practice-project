package com.cs.core.runtime.interactor.model.filter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class PropertyInstanceRoleTypeFilterModel
    extends AbstractPropertyInstanceFilterModel<IFilterValueRoleModel>
    implements IPropertyInstanceRoleTypeFilterModel {
  
  @Override
  public List<IFilterValueRoleModel> getMandatory()
  {
    return this.mandatory;
  }
  
  @JsonDeserialize(contentAs = FilterValueRoleModel.class)
  @Override
  public void setMandatory(List<IFilterValueRoleModel> mandatory)
  {
    this.mandatory = mandatory;
  }
  
  @Override
  public List<IFilterValueRoleModel> getShould()
  {
    return this.should;
  }
  
  @Override
  public void setShould(List<IFilterValueRoleModel> should)
  {
    this.should = should;
  }
}
