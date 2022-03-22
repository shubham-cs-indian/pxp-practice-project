package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.attribute.IAttributeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISortDataModel extends IModel {
  
  public static final String ATTRIBUTES = "attributes";
  
  public List<IAttributeInformationModel> getAttributes();
  
  public void setAttributes(List<IAttributeInformationModel> attributes);
}
