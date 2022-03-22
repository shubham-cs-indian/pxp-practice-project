package com.cs.core.config.interactor.entity.attribute;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ConcatenatedAttribute extends AbstractAttribute implements IConcatenatedAttribute {
  
  private static final long             serialVersionUID = 1L;
  
  protected List<IConcatenatedOperator> attributeConcatenatedList;
  protected Boolean                     isCodeVisible    = false;
  
  @Override
  public List<IConcatenatedOperator> getAttributeConcatenatedList()
  {
    if (attributeConcatenatedList == null) {
      attributeConcatenatedList = new ArrayList<>();
    }
    return attributeConcatenatedList;
  }
  
  @JsonDeserialize(contentAs = AbstractConcatenatedOperator.class)
  @Override
  public void setAttributeConcatenatedList(List<IConcatenatedOperator> attributeConcatenatedList)
  {
    this.attributeConcatenatedList = attributeConcatenatedList;
  }
  
  @Override
  public String getRendererType()
  {
    return Renderer.CONCATENATED.name();
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
