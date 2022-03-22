package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.klass.IInheritanceDataModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IValueInheritancePropagationModel extends IModel {
  
  public static final String INHERITANCE_DATA = "inheritanceData";
  
  public List<IInheritanceDataModel> getInheritanceData();
  
  public void setInheritanceData(List<IInheritanceDataModel> inheritanceData);
}
