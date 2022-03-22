package com.cs.core.runtime.interactor.model.attribute;

public class AttributeVariantsStatsModel implements IAttributeVariantsStatsModel {
  
  private static final long serialVersionUID = 1L;
  private Double            max;
  private Double            min;
  private Double            sum;
  private Double            avg;
  private Long              count;
  private String            attributeId;
  
  @Override
  public Double getMax()
  {
    return max;
  }
  
  @Override
  public void setMax(Double max)
  {
    this.max = max;
  }
  
  @Override
  public Double getMin()
  {
    return min;
  }
  
  @Override
  public void setMin(Double min)
  {
    this.min = min;
  }
  
  @Override
  public Double getSum()
  {
    return sum;
  }
  
  @Override
  public void setSum(Double sum)
  {
    this.sum = sum;
  }
  
  @Override
  public Double getAvg()
  {
    return avg;
  }
  
  @Override
  public void setAvg(Double avg)
  {
    this.avg = avg;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
}
