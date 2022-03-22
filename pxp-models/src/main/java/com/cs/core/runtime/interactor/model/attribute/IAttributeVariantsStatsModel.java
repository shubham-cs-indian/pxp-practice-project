package com.cs.core.runtime.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAttributeVariantsStatsModel extends IModel {
  
  public static final String MAX          = "max";
  public static final String MIN          = "min";
  public static final String SUM          = "sum";
  public static final String AVG          = "avg";
  public static final String COUNT        = "count";
  public static final String ATTRIBUTE_ID = "attributeId";
  
  public Double getMax();
  
  public void setMax(Double max);
  
  public Double getMin();
  
  public void setMin(Double min);
  
  public Double getSum();
  
  public void setSum(Double sum);
  
  public Double getAvg();
  
  public void setAvg(Double avg);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
}
