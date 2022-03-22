package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetAllModel extends AbstractAdditionalPropertiesModel implements IGetAllModel {
  
  protected Integer          from;
  
  protected Integer          size;
  
  protected String           searchString;
  
  protected List<ISortModel> sortOptions;
  
  public GetAllModel()
  {
  }
  
  public GetAllModel(Integer from, Integer size, String searchString)
  {
    this.from = from;
    this.size = size;
    this.searchString = searchString;
  }
  
  @Override
  public Integer getFrom()
  {
    return this.from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return this.size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public String getSearchString()
  {
    return this.searchString;
  }
  
  @Override
  public void setSearchString(String searchString)
  {
    this.searchString = searchString;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"from\":\"" + from + "\",");
    strBuilder.append("\"size\":\"" + size + "\"");
    strBuilder.append("\"searchString\":\"" + searchString + "\"");
    strBuilder.append("}");
    
    return strBuilder.toString();
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
