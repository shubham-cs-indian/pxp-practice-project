package com.cs.dam.config.interactor.model.downloadtracker;

import java.util.ArrayList;
import java.util.List;

public class GetLabelsByIdsRequestModel implements IGetLabelsByIdsRequestModel {
  
  private static final long serialVersionUID = 1L;
  private List<String>      ids              = new ArrayList<>();
  private String            entityType;
  private String            searchText;
  private Long              from;
  private Long              size;
  private String            sortOrder;

  @Override
  public List<String> getIds()
  {
    return ids;
  }

  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }

  @Override
  public String getEntityType()
  {
    return entityType;
  }

  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }

  @Override
  public String getSearchText()
  {
    return searchText;
  }

  @Override
  public void setSearchText(String searchText)
  {
    this.searchText = searchText;
  }

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
  public String getSortOrder()
  {
    return sortOrder;
  }

  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
}
