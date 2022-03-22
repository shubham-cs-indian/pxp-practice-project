package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;

import java.util.List;

public interface IAttributeRecommendationModel extends IRecommendationModel {
  
  public static final String VALUE               = "value";
  public static final String VALUE_AS_HTML       = "valueAsHtml";
  public static final String VALUE_AS_EXPRESSION = "valueAsExpression";
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public List<IConcatenatedOperator> getValueAsExpression();
  
  public void setValueAsExpression(List<IConcatenatedOperator> valueAsExpression);
}
