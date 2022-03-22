package com.cs.core.config.interactor.model.attribute;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBulkSaveAttributeSuccessModel extends IModel {
  
  public static final String ATTRIBUTES_LIST        = "attributesList";
  public static final String CONFIG_DETAILS         = "configDetails";
  public static final String UPDATED_ATTRIBUTE_LIST = "updatedAttributeList";
  
  public List<IAttributeModel> getAttributesList();
  
  public void setAttributesList(List<IAttributeModel> attributesList);
  
  public IConfigDetailsForGridAttributesModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridAttributesModel configDetails);
  
  public List<IUpdatedAttributeListModel> getUpdatedAttributeList();
  
  public void setUpdatedAttributeList(List<IUpdatedAttributeListModel> updatedAttributeList);
}
