package com.cs.core.config.interactor.model.klass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ConflictingValueModel implements IConflictingValueModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected String              couplingType;
  protected List<IValueIdModel> values           = new ArrayList<>();
  
  @Override
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
  
  @Override
  public List<IValueIdModel> getValues()
  {
    return values;
  }
  
  @JsonDeserialize(contentAs = ValueIdModel.class)
  @Override
  public void setValues(List<IValueIdModel> values)
  {
    this.values = values;
  }
}
