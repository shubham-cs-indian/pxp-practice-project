package com.cs.core.config.interactor.entity.standard.attribute;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStandardAttribute extends AbstractAttribute
    implements IStandardAttribute {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    klassType;
  
  public AbstractStandardAttribute()
  {
  }
  
  public AbstractStandardAttribute(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<String> getKlassType()
  {
    if (klassType == null) {
      klassType = new ArrayList<String>();
    }
    return klassType;
  }
  
  @Override
  public void setKlassType(List<String> klassType)
  {
    this.klassType = klassType;
  }
  
  @Override
  public String getRendererType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
