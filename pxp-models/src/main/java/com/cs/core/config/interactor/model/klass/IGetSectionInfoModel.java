package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetSectionInfoModel extends IModel {
  
  public static final String LIST          = "list";
  public static final String CONFIGDETAILS = "configDetails";
  
  public List<ISection> getList();
  
  public void setList(List<ISection> list);
  
  public IGetSectionInfoConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetSectionInfoConfigDetailsModel configDetails);
}
