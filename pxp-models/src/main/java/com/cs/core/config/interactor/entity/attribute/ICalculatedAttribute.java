package com.cs.core.config.interactor.entity.attribute;

import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;

import java.util.List;

public interface ICalculatedAttribute extends IAttribute {
  
  public static final String ATTRIBUTE_OPERATOR_LIST           = "attributeOperatorList";
  
  public static final String PRECISION                         = "precision";
  
  public static final String CALCULATED_ATTRIBUTE_TYPE         = "calculatedAttributeType";
  
  public static final String CALCULATED_ATTRIBUTE_UNIT         = "calculatedAttributeUnit";
  
  public static final String CALCULATED_ATTRIBUTE_UNIT_AS_HTML = "calculatedAttributeUnitAsHTML";
  
  public static final String HIDE_SEPARATOR                    = "hideSeparator";
  
  public List<IAttributeOperator> getAttributeOperatorList();
  
  public void setAttributeOperatorList(List<IAttributeOperator> attributeOperatorList);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer Precision);
  
  public String getCalculatedAttributeType();
  
  public void setCalculatedAttributeType(String calculatedAttributeType);
  
  public String getCalculatedAttributeUnit();
  
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit);
  
  public String getCalculatedAttributeUnitAsHTML();
  
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML);
  
  public Boolean getHideSeparator();
  
  public void setHideSeparator(Boolean hideSeparator);
}
