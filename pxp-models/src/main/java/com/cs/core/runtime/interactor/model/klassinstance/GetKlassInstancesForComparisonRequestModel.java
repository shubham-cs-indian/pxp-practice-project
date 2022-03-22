package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetKlassInstancesForComparisonRequestModel
    implements IGetKlassInstancesForComparisonRequestModel {
  
  protected List<String>     ids;
  protected String           klassInstanceId;
  protected Integer          from;
  protected Integer          size;
  protected List<ISortModel> sortOptions;
  
  public GetKlassInstancesForComparisonRequestModel()
  {
  }
  
  public GetKlassInstancesForComparisonRequestModel(Integer from, Integer size)
  {
    super();
    this.from = from;
    this.size = size;
  }
  
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<String>();
    }
    return ids;
  }
  
  public void setIds(List<String> ids)
  {
    this.ids = ids;
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
  
  @JsonIgnore
  @Override
  public String getSearchString()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setSearchString(String searchString)
  {
  }
  
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @JsonDeserialize(contentAs = SortModel.class)
  @Override
  public void setSortOptions(List<ISortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
  }
}
