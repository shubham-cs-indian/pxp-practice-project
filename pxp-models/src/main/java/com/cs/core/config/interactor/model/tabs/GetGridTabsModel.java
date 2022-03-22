package com.cs.core.config.interactor.model.tabs;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetGridTabsModel implements IGetGridTabsModel {
  
  private static final long          serialVersionUID = 1L;
  protected List<IGetTabEntityModel> tabList;
  protected Long                     count;
  
  @Override
  public List<IGetTabEntityModel> getTabList()
  {
    return tabList;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetTabEntityModel.class)
  public void setTabList(List<IGetTabEntityModel> tabList)
  {
    this.tabList = tabList;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
