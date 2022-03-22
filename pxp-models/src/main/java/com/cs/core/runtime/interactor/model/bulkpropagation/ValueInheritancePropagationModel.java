package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.IInheritanceDataModel;
import com.cs.core.runtime.interactor.model.configdetails.InheritanceDataModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ValueInheritancePropagationModel implements IValueInheritancePropagationModel {
  
  private static final long             serialVersionUID = 1L;
  
  protected List<IInheritanceDataModel> inheritanceData  = new ArrayList<>();
  
  @Override
  public List<IInheritanceDataModel> getInheritanceData()
  {
    return inheritanceData;
  }
  
  @JsonDeserialize(contentAs = InheritanceDataModel.class)
  @Override
  public void setInheritanceData(List<IInheritanceDataModel> inheritanceData)
  {
    this.inheritanceData = inheritanceData;
  }
}
