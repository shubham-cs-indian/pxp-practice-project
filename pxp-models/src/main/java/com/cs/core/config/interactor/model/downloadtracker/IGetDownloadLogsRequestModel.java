package com.cs.core.config.interactor.model.downloadtracker;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGridViewFilterModel;

public interface IGetDownloadLogsRequestModel extends IModel {
  
  public static final String FROM                   = "from";
  public static final String SIZE                   = "size";
  public static final String SORT_OPTIONS           = "sortOptions";
  public static final String GRID_VIEW_FILTER_MODEL = "gridViewFilterModel";
  public static final String PRIMARY_KEYS           = "primaryKeys";
  
  public Long getFrom();
  public void setFrom(Long from);
  
  public Long getSize();
  public void setSize(Long size);
  
  public ISortModel getSortOptions();
  public void setSortOptions(ISortModel sortOptions);
  
  public List<String> getPrimaryKeys();
  public void setPrimaryKeys(List<String> primaryKeys);
  
  public List<? extends IGridViewFilterModel> getGridViewFilterModel();
  public void setGridViewFilterModel(List<? extends IGridViewFilterModel> gridViewFilterModel);
 
}
