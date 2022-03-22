package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;

public abstract class AbstractUnitAttributeModel extends AbstractAttributeModel
    implements IUnitAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public AbstractUnitAttributeModel(IAttribute attribute, String renderer)
  {
    super(attribute, renderer);
  }
  
  @Override
  public String getDefaultUnit()
  {
    return ((IUnitAttribute) attribute).getDefaultUnit();
  }
  
  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    ((IUnitAttribute) attribute).setDefaultUnit(defaultUnit);
  }
  
  @Override
  public Integer getPrecision()
  {
    return ((IUnitAttribute) attribute).getPrecision();
  }
  
  @Override
  public void setPrecision(Integer precision)
  {
    ((IUnitAttribute) attribute).setPrecision(precision);
  }
  
  @Override
  public Boolean getHideSeparator()
  {
    return ((IUnitAttribute) attribute).getHideSeparator();
  }
  
  @Override
  public void setHideSeparator(Boolean hideSeparator)
  {
    ((IUnitAttribute) attribute).setHideSeparator(hideSeparator);
  }
}
