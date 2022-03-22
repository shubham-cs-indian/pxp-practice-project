package com.cs.core.config.interactor.entity.attribute;

import java.util.List;

public interface IConcatenatedAttribute extends IAttribute {
  
  public static final String ATTRIBUTE_CONCATENATED_LIST = "attributeConcatenatedList";
  public static final String IS_CODE_VISIBLE             = "isCodeVisible";
  
  public List<IConcatenatedOperator> getAttributeConcatenatedList();
  
  public void setAttributeConcatenatedList(List<IConcatenatedOperator> attributeConcatenatedList);
  
  public Boolean getIsCodeVisible();
  
  public void setIsCodeVisible(Boolean isCodeVisible);
}
