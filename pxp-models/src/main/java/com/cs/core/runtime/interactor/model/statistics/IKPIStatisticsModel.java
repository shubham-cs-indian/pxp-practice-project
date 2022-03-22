package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKPIStatisticsModel extends IModel {
  
  public static final String KPI_ID        = "kpiId";
  public static final String LEVEL_ID      = "levelId";
  public static final String CATEGORIES    = "categories";
  public static final String CATEGORY_TYPE = "categoryType";
  public static final String PATH          = "path";
  public static final String TYPE_IDS      = "typeIds";
  
  public String getKpiId();
  
  public void setKpiId(String kpi);
  
  public String getLevelId();
  
  public void setLevelId(String levelId);
  
  public List<ICategoryModel> getCategories();
  
  public void setCategories(List<ICategoryModel> categories);
  
  public String getCategoryType();
  
  public void setCategoryType(String categoryType);
  
  public List<IPathModel> getPath();
  
  public void setPath(List<IPathModel> path);
  
  public List<String> getTypeIds();
  
  public void setTypeIds(List<String> typeIds);
}
