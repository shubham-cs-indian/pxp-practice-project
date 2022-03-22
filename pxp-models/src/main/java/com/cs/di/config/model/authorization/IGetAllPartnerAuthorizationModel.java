package com.cs.di.config.model.authorization;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllPartnerAuthorizationModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IConfigEntityInformationModel> getList();
  
  public void setList(List<IConfigEntityInformationModel> list);
}
