package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;

import java.util.List;

public class AttributeRecommendationModel extends AbstractRecommendationModel
    implements IAttributeRecommendationModel {
  
  private static final long             serialVersionUID = 1L;
  protected String                      value;
  protected String                      valueAsHtml;
  protected List<IConcatenatedOperator> valueAsExpression;
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
  
  @Override
  public List<IConcatenatedOperator> getValueAsExpression()
  {
    return valueAsExpression;
  }
  
  @Override
  public void setValueAsExpression(List<IConcatenatedOperator> valueAsExpression)
  {
    this.valueAsExpression = valueAsExpression;
  }
}
