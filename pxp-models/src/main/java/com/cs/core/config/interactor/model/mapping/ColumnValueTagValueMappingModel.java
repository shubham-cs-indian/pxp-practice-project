package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ColumnValueTagValueMappingModel implements IColumnValueTagValueMappingModel {
  
  private static final long             serialVersionUID = 1L;
  protected String                      columnName;
  protected List<ITagValueMappingModel> mappings         = new ArrayList<>();
  protected List<String>                tagValueIds      = new ArrayList<>();
  
  @Override
  public String getColumnName()
  {
    
    return columnName;
  }
  
  @Override
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }
  
  @Override
  public List<ITagValueMappingModel> getMappings()
  {
    
    return mappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagValueMappingModel.class)
  public void setMappings(List<ITagValueMappingModel> mappings)
  {
    this.mappings = mappings;
  }
  
  @Override
  public List<String> getTagValueIds()
  {
    
    return tagValueIds;
  }
  
  @Override
  public void setTagValueIds(List<String> tagValueIds)
  {
    
    this.tagValueIds = tagValueIds;
  }
}
