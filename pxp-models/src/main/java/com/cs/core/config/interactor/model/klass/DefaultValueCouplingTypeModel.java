package com.cs.core.config.interactor.model.klass;

public class DefaultValueCouplingTypeModel implements IDefaultValueCouplingTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          couplingType;
  protected Boolean         isMandatory;
  protected Boolean         isShould;
  protected Boolean         isSkipped;
  protected Boolean         isValueChanged;
  protected Boolean         isCouplingTypeChanged;
  
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
  public Boolean getIsMandatory()
  {
    return isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public Boolean getIsShould()
  {
    return isShould;
  }
  
  @Override
  public void setIsShould(Boolean isShould)
  {
    this.isShould = isShould;
  }
  
  @Override
  public Boolean getIsSkipped()
  {
    if (isSkipped == null) {
      isSkipped = false;
    }
    return isSkipped;
  }
  
  @Override
  public void setIsSkipped(Boolean isSkipped)
  {
    this.isSkipped = isSkipped;
  }
  
  @Override
  public Boolean getIsValueChanged()
  {
    if (isValueChanged == null) {
      isValueChanged = false;
    }
    return isValueChanged;
  }
  
  @Override
  public void setIsValueChanged(Boolean isValueChanged)
  {
    this.isValueChanged = isValueChanged;
  }
  
  @Override
  public Boolean getIsCouplingTypeChanged()
  {
    if (isCouplingTypeChanged == null) {
      isCouplingTypeChanged = false;
    }
    return isCouplingTypeChanged;
  }
  
  @Override
  public void setIsCouplingTypeChanged(Boolean isCouplingTypeChanged)
  {
    this.isCouplingTypeChanged = isCouplingTypeChanged;
  }
}
