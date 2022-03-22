package com.cs.core.config.interactor.model.downloadtracker;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.cs.dam.config.interactor.model.downloadtracker.GridViewFilterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGridViewFilterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetDownloadLogsRequestModel implements IGetDownloadLogsRequestModel {
  
  private static final long                      serialVersionUID    = 1L;
  protected Long                                 from;
  protected Long                                 size;
  protected ISortModel                           sortOptions         = new SortModel();
  protected List<String>                         primaryKeys         = new ArrayList<>();
  protected List<? extends IGridViewFilterModel> gridViewFilterModel = new ArrayList<>();
  
  @Override
  public Long getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Long from)
  {
    this.from = from;
  }
  
  @Override
  public Long getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Long size)
  {
    this.size = size;
  }
  
  @Override
  public ISortModel getSortOptions()
  {
    return sortOptions;
  }
  
  @Override
  @JsonDeserialize(as = SortModel.class)
  public void setSortOptions(ISortModel sortOptions)
  {
    this.sortOptions = sortOptions;
  }
  
  @Override
  public List<String> getPrimaryKeys()
  {
    return primaryKeys;
  }
  
  @Override
  public void setPrimaryKeys(List<String> primaryKeys)
  {
    this.primaryKeys = primaryKeys;
  }
  @Override
  public List<? extends IGridViewFilterModel> getGridViewFilterModel()
  {
    return gridViewFilterModel;
  }
  
  @Override
  @JsonDeserialize(contentAs = GridViewFilterModel.class)
  public void setGridViewFilterModel(List<? extends IGridViewFilterModel> gridViewFilterModel)
  {
    this.gridViewFilterModel = gridViewFilterModel;
  }
  
}
