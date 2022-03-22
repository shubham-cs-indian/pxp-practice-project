package com.cs.config.strategy.plugin.model.mapping;

public class MappingHelperModel implements IMappingHelperModel {
  
  private static final long                  serialVersionUID = 1L;
  
  protected IMappingConfigDetailsHelperModel configDetails;
  protected String                           selectedPropertyCollectionId;
  protected String                           tabId;
  protected Boolean                          isExport;
  protected String                           selectedContextId;
  
  
  public IMappingConfigDetailsHelperModel getConfigDetails()
  {
    if (configDetails == null) {
      configDetails = new MappingConfigDetailsHelperModel();
    }
    return configDetails;
  }
  
  public void setConfigDetails(IMappingConfigDetailsHelperModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public String getSelectedPropertyCollectionId()
  {
    return selectedPropertyCollectionId;
  }
  
  @Override
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId)
  {
    this.selectedPropertyCollectionId = selectedPropertyCollectionId;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public Boolean getIsExport()
  {
    if (isExport == null) {
      isExport = false;
    }
    return isExport;
  }
  
  @Override
  public void setIsExport(Boolean isExport)
  {
    this.isExport = isExport;
  }

  @Override
  public String getSelectedContextId()
  {
    return selectedContextId;
  }

  @Override
  public void setSelectedContextId(String selectedContextId)
  {
    this.selectedContextId = selectedContextId;
  }
}
