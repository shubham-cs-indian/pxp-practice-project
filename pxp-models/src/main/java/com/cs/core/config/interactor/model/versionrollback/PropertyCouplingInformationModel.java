package com.cs.core.config.interactor.model.versionrollback;

public class PropertyCouplingInformationModel implements IPropertyCouplingInformationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          couplingType;
  protected Object          defaultValue;
  
  @Override
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
  
  @Override
  public Object getDefaultValue()
  {
    return defaultValue;
  }
  
  @Override
  public void setDefaultValue(Object defaultValue)
  {
    this.defaultValue = defaultValue;
  }
}
