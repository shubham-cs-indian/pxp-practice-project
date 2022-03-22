package com.cs.pim.runtime.interactor.model.articleinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICreateAttributeInstanceStrategyModel extends IModel {
  
  public static final String ATTRIBUTES = "attributes";
  
  public List<? extends IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<? extends IContentAttributeInstance> attributes);
}
