package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IColumnValueTagValueMappingModel extends IModel {
  
  public static final String COLUMN_NAME   = "columnName";
  public static final String MAPPINGS      = "mappings";
  public static final String TAG_VALUE_IDS = "tagValueIds";
  
  public String getColumnName();
  
  public void setColumnName(String columnName);
  
  public List<ITagValueMappingModel> getMappings();
  
  public void setMappings(List<ITagValueMappingModel> mappings);
  
  public List<String> getTagValueIds();
  
  public void setTagValueIds(List<String> tagValueIds);
}
