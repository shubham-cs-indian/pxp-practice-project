package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;

import java.util.List;

public interface IConcatenatedNormalization extends INormalization {
  
  public static final String ATTRIBUTE_CONCATENATED_LIST = "attributeConcatenatedList";
  
  public List<IConcatenatedOperator> getAttributeConcatenatedList();
  
  public void setAttributeConcatenatedList(List<IConcatenatedOperator> attributeConcatenatedList);
}
