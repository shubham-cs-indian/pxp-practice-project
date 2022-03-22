package com.cs.core.config.interactor.model.tabs;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGridTabsModel extends IModel {
  
  public static final String TAB_LIST = "tabList";
  public static final String COUNT    = "count";
  
  public List<IGetTabEntityModel> getTabList();
  
  public void setTabList(List<IGetTabEntityModel> tabList);
  
  public Long getCount();
  
  public void setCount(Long count);
}
