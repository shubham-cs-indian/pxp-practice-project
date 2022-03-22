package com.cs.core.config.interactor.model.attribute;

import java.util.List;

import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUpdatedAttributeListModel extends IModel {
  
  public static final String ATTRIBUTE_CONCATENATED_LIST = "attributeConcatenatedList";
  public static final String PROPERTY_IID                = "propertyIID";
  public static final String ATTRIBUTE_ID                = "attributeId";
  
  public List<IConcatenatedOperator> getAttributeConcatenatedList();
  
  public void setAttributeConcatenatedList(List<IConcatenatedOperator> attributeConcatenatedList);
  
  public String getPropertyIID();
  
  public void setPropertyIID(String propertyIID);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
}
