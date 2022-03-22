package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetStatisticsRequestModel extends IModel {
  
  public static final String LEVEL_ID = "levelId";
  public static final String KPI_ID   = "kpiId";
  public static final String PATH     = "path";
  public static final String FROM     = "from";
  public static final String SIZE     = "size";
  
  public String getLevelId();
  
  public void setLevelId(String levelId);
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
  
  public List<IPathModel> getPath();
  
  public void setPath(List<IPathModel> path);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
}
