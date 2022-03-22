package com.cs.core.runtime.interactor.model.statistics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class KPIStatisticsModel implements IKPIStatisticsModel {
  
  private static final long      serialVersionUID = 1L;
  protected String               kpiId;
  protected String               levelId;
  protected List<ICategoryModel> categories;
  protected String               categoryType;
  protected List<IPathModel>     path;
  protected List<String>         typeIds          = new ArrayList<>();
  
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
  public List<ICategoryModel> getCategories()
  {
    return categories;
  }
  
  @JsonDeserialize(contentAs = CategoryModel.class)
  @Override
  public void setCategories(List<ICategoryModel> categories)
  {
    this.categories = categories;
  }
  
  @Override
  public String getCategoryType()
  {
    return categoryType;
  }
  
  @Override
  public void setCategoryType(String categoryType)
  {
    this.categoryType = categoryType;
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
  public List<String> getTypeIds()
  {
    return typeIds;
  }
  
  @Override
  public void setTypeIds(List<String> typeIds)
  {
    this.typeIds = typeIds;
  }
}
