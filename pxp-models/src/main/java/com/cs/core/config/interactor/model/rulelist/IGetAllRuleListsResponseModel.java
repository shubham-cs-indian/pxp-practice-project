package com.cs.core.config.interactor.model.rulelist;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllRuleListsResponseModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IRuleListInformationModel> getList();
  
  public void setList(List<IRuleListInformationModel> list);
}
