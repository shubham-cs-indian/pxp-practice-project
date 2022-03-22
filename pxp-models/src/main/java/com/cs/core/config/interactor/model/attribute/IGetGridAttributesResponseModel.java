package com.cs.core.config.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGridAttributesResponseModel extends IModel {
  
  public static final String ATTRIBUTE_LIST = "attributeList";
  public static final String COUNT          = "count";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public List<IAttributeModel> getAttributeList();
  
  public void setAttributeList(List<IAttributeModel> attributeList);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public IConfigDetailsForGridAttributesModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridAttributesModel configDetails);
}
