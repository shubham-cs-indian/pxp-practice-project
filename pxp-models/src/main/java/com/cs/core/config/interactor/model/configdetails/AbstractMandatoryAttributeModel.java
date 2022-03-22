package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.entity.standard.attribute.IStandardAttribute;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AbstractMandatoryAttributeModel extends AbstractAttributeModel
    implements IMandatoryAttributeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          code;
  
  public AbstractMandatoryAttributeModel(IStandardAttribute attribute, String renderer)
  {
    super(attribute, renderer);
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public List<String> getKlassType()
  {
    return ((IStandardAttribute) attribute).getKlassType();
  }
  
  @Override
  public void setKlassType(List<String> klassType)
  {
    ((IStandardAttribute) attribute).setKlassType(klassType);
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
}
