package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IAttributeModel extends IConfigModel, IAttribute {
  
  public String getRendererType();
}
