package com.cs.core.config.interactor.model.attribute.standard;

import java.util.List;

import com.cs.core.config.interactor.entity.attribute.CalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.datarule.AttributeOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveCalculatedAttributeModel extends AbstractSaveAttributeModel
    implements ICalculatedAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public SaveCalculatedAttributeModel()
  {
    super(new CalculatedAttribute(), Renderer.CALCULATED.toString());
  }
  
  public SaveCalculatedAttributeModel(ICalculatedAttribute attribute)
  {
    super(attribute, Renderer.CALCULATED.toString());
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<IAttributeOperator> getAttributeOperatorList()
  {
    return ((ICalculatedAttribute) attribute).getAttributeOperatorList();
  }
  
  @JsonDeserialize(contentAs = AttributeOperator.class)
  @Override
  public void setAttributeOperatorList(List<IAttributeOperator> attributeOperatorList)
  {
    ((ICalculatedAttribute) attribute).setAttributeOperatorList(attributeOperatorList);
  }
  
  @Override
  public Integer getPrecision()
  {
    return ((ICalculatedAttribute) attribute).getPrecision();
  }
  
  @Override
  public void setPrecision(Integer Precision)
  {
    ((ICalculatedAttribute) attribute).setPrecision(Precision);
  }
  
  @Override
  public String getCalculatedAttributeType()
  {
    return ((ICalculatedAttribute) attribute).getCalculatedAttributeType();
  }
  
  @Override
  public void setCalculatedAttributeType(String calculatedAttributeType)
  {
    ((ICalculatedAttribute) attribute).setCalculatedAttributeType(calculatedAttributeType);
  }
  
  @Override
  public String getCalculatedAttributeUnit()
  {
    return ((ICalculatedAttribute) attribute).getCalculatedAttributeUnit();
  }
  
  @Override
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit)
  {
    ((ICalculatedAttribute) attribute).setCalculatedAttributeUnit(calculatedAttributeUnit);
  }
  
  @Override
  public String getCalculatedAttributeUnitAsHTML()
  {
    return ((ICalculatedAttribute) attribute).getCalculatedAttributeUnitAsHTML();
  }
  
  @Override
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML)
  {
    ((ICalculatedAttribute) attribute)
        .setCalculatedAttributeUnitAsHTML(calculatedAttributeUnitAsHTML);
    ;
  }

  @Override
  public Boolean getHideSeparator()
  {
    return ((ICalculatedAttribute) attribute).getHideSeparator();
  }
  
  @Override
  public void setHideSeparator(Boolean hideSeparator)
  {
    ((ICalculatedAttribute) attribute).setHideSeparator(hideSeparator);
  }
}
