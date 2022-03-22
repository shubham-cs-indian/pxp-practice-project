package com.cs.pim.runtime.interactor.model.articleinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CreateAttributeInstanceStrategyModel implements ICreateAttributeInstanceStrategyModel {
  
  private static final long                 serialVersionUID = 1L;
  protected List<IContentAttributeInstance> attributes;
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return this.attributes;
  }
  
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    this.attributes = (List<IContentAttributeInstance>) attributes;
  }
}
