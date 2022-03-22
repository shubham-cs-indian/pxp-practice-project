package com.cs.core.config.interactor.model.mapping;

import java.util.List;

public class ConfigRuleAttributeMappingModel implements IConfigRuleAttributeMappingModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    columnNames;
  protected String          mappedElementId;
  protected Boolean         isIgnored;
  protected Boolean         isAutomapped     = true;
  
  @Override
  public Boolean getIsAutomapped()
  {
    return isAutomapped;
  }
  
  @Override
  public void setIsAutomapped(Boolean isAutomapped)
  {
    this.isAutomapped = isAutomapped;
  }
  
  @Override
  public String getId()
  {
    
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getColumnNames()
  {
    
    return columnNames;
  }
  
  @Override
  public void setColumnNames(List<String> columnNames)
  {
    this.columnNames = columnNames;
  }
  
  @Override
  public String getMappedElementId()
  {
    
    return mappedElementId;
  }
  
  @Override
  public void setMappedElementId(String mappedElementId)
  {
    this.mappedElementId = mappedElementId;
  }
  
  @Override
  public Boolean getIsIgnored()
  {
    
    return isIgnored;
  }
  
  @Override
  public void setIsIgnored(Boolean isIgnored)
  {
    this.isIgnored = isIgnored;
  }
}
