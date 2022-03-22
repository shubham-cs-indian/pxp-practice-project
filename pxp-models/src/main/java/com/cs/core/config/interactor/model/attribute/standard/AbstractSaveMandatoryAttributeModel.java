package com.cs.core.config.interactor.model.attribute.standard;

import java.util.List;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.standard.attribute.IStandardAttribute;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AbstractSaveMandatoryAttributeModel extends AbstractSaveAttributeModel
    implements IMandatoryAttributeModel {
  
  private static final long serialVersionUID = 1L;
  
  public AbstractSaveMandatoryAttributeModel(IAttribute attribute, String renderer)
  {
    super(attribute, renderer);
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
