package com.cs.dam.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class AssetInstanceFilterModel implements IAssetInstanceFilterModel {
  
  protected List<IAssetPropertyInstanceFilterModel> attributes;
  
  protected List<IAssetPropertyInstanceFilterModel> tags;
  
  protected String                                  sortField;
  
  protected String                                  sortOrder;
  
  protected String                                  allSearch;
  
  @Override
  public List<IAssetPropertyInstanceFilterModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  @Override
  public void setAttributes(List<IAssetPropertyInstanceFilterModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public String getSortField()
  {
    return sortField;
  }
  
  @Override
  public void setSortField(String sortField)
  {
    this.sortField = sortField;
  }
  
  @Override
  public String getSortOrder()
  {
    return sortOrder;
  }
  
  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
  @Override
  public String getAllSearch()
  {
    return allSearch;
  }
  
  @Override
  public void setAllSearch(String allSearch)
  {
    this.allSearch = allSearch;
  }
  
  @Override
  public List<IAssetPropertyInstanceFilterModel> getTags()
  {
    if (this.tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<IAssetPropertyInstanceFilterModel> tags)
  {
    this.tags = tags;
  }
}
