package com.cs.core.config.interactor.model.mapping;

import java.util.List;

public class ConfigRulePropertyMappingModel implements IConfigRulePropertyMappingModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    columnNames;
  protected String          mappedElementId;
  protected Boolean         isIgnored;
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getId()
  {
    
    return id;
  }
  
  @Override
  public void setColumnNames(List<String> columnNames)
  {
    this.columnNames = columnNames;
  }
  
  @Override
  public List<String> getColumnNames()
  {
    
    return columnNames;
  }
  
  @Override
  public void setMappedElementId(String mappedElementId)
  {
    this.mappedElementId = mappedElementId;
  }
  
  @Override
  public String getMappedElementId()
  {
    
    return mappedElementId;
  }
  
  @Override
  public void setIsIgnored(Boolean isIgnored)
  {
    this.isIgnored = isIgnored;
  }
  
  @Override
  public Boolean getIsIgnored()
  {
    
    return isIgnored;
  }
  
}
