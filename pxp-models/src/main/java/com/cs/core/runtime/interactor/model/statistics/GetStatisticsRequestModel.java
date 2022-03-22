package com.cs.core.runtime.interactor.model.statistics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetStatisticsRequestModel implements IGetStatisticsRequestModel {
  
  private static final long  serialVersionUID = 1L;
  protected String           levelId;
  protected String           kpiId;
  protected List<IPathModel> path;
  protected Integer          from;
  protected Integer          size;
  
  @Override
  public String getLevelId()
  {
    return levelId;
  }
  
  @Override
  public void setLevelId(String levelId)
  {
    this.levelId = levelId;
  }
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
  
  @Override
  public List<IPathModel> getPath()
  {
    return path;
  }
  
  @JsonDeserialize(contentAs = PathModel.class)
  @Override
  public void setPath(List<IPathModel> path)
  {
    this.path = path;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
}
