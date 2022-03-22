package com.cs.core.config.interactor.model.asset;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllIconsResponseModel extends IModel {
  
  public static final String ICONS       = "icons";
  public static final String TOTAL_COUNT = "totalCount";
  public static final String FROM        = "from";  
  public List<IIconModel> getIcons();
  
  public void setIcons(List<IIconModel> icons);
  
  public Integer getTotalCount();
  
  public void setTotalCount(Integer totalCount);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
}
