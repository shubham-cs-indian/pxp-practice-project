package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.attribute.AbstractConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.datarule.IConcatenatedNormalization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ConcatenatedNormalization extends Normalization implements IConcatenatedNormalization {
  
  private static final long             serialVersionUID = 1L;
  
  protected List<IConcatenatedOperator> attributeConcatenatedList;
  
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
}
