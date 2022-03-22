package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBasicFilterRequestModel extends IModel {
  
  public static final String ATTRIBUTES   = "attributes";
  public static final String TAGS         = "tags";
  public static final String ALL_SEARCH   = "allSearch";
  public static final String SORT_OPTIONS = "sortOptions";
  public static final String FROM         = "from";
  public static final String SIZE         = "size";
  public static final String TIME_RANGE   = "timeRange";
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public List<? extends IPropertyInstanceFilterModel> getAttributes();
  
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes);
  
  public List<? extends IPropertyInstanceFilterModel> getTags();
  
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
}
