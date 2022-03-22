package com.cs.core.runtime.interactor.model.process;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTranslatableAttributesModel extends IModel {
  
  public static final String MODULE_ID     = "moduleId";
  public static final String ATTRIBUTE_IDS = "attributeIds";
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
}
