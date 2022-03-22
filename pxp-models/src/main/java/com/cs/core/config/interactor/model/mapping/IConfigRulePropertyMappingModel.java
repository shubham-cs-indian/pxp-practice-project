package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigRulePropertyMappingModel extends IModel {
  
  public static final String ID                = "id";
  public static final String COLUMN_NAMES      = "columnNames";
  public static final String MAPPED_ELEMENT_ID = "mappedElementId";
  public static final String IS_IGNORED        = "isIgnored";
  
  public void setId(String id);
  
  public String getId();
  
  public void setColumnNames(List<String> columnNames);
  
  public List<String> getColumnNames();
  
  public void setIsIgnored(Boolean isIgnored);
  
  public Boolean getIsIgnored();
  
  public void setMappedElementId(String mappedElementId);
  
  public String getMappedElementId();
  
}
