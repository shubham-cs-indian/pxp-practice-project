package com.cs.core.config.interactor.model.attribute.standard;

import java.util.List;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.ConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConcatenatedAttributeModel extends AbstractAttributeModel
    implements IConcatenatedAttributeModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isCodeVisible    = false;
  
  public ConcatenatedAttributeModel()
  {
    super(new ConcatenatedAttribute(), Renderer.CONCATENATED.toString());
  }
  
  public ConcatenatedAttributeModel(IConcatenatedAttribute attribute)
  {
    super(attribute, Renderer.CONCATENATED.toString());
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
  public List<IConcatenatedOperator> getAttributeConcatenatedList()
  {
    return ((IConcatenatedAttribute) attribute).getAttributeConcatenatedList();
  }
  
  @JsonDeserialize(contentAs = AbstractConcatenatedOperator.class)
  @Override
  public void setAttributeConcatenatedList(List<IConcatenatedOperator> attributeConcatenatedList)
  {
    ((IConcatenatedAttribute) attribute).setAttributeConcatenatedList(attributeConcatenatedList);
  }
  
  @Override
  public Boolean getIsCodeVisible()
  {
    return isCodeVisible;
  }
  
  @Override
  public void setIsCodeVisible(Boolean isCodeVisible)
  {
    this.isCodeVisible = isCodeVisible;
  }
}
