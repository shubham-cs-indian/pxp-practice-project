package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IConfigRuleAttributeMappingModel extends IModel {
  
  public static final String ID                = "id";
  public static final String COLUMN_NAMES      = "columnNames";
  public static final String MAPPED_ELEMENT_ID = "mappedElementId";
  public static final String IS_IGNORED        = "isIgnored";
  public static final String IS_AUTOMAPPED     = "isAutomapped";
  
  public Boolean getIsAutomapped();
  
  public void setIsAutomapped(Boolean isAutomapped);
  
  public String getId();
  
  public void setId(String id);
  
  public List<String> getColumnNames();
  
  public void setColumnNames(List<String> columnNames);
  
  public String getMappedElementId();
  
  public void setMappedElementId(String mappedElementId);
  
  public Boolean getIsIgnored();
  
  public void setIsIgnored(Boolean isIgnored);
}
