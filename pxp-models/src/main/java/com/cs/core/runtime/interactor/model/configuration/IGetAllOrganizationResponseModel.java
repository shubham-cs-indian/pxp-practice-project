package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.config.interactor.model.organization.IOrganizationModel;

import java.util.List;

public interface IGetAllOrganizationResponseModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public List<IOrganizationModel> getList();
  
  public void setList(List<IOrganizationModel> list);
  
  public Long getCount();
  
  public void setCount(Long count);
}
