package com.cs.config.strategy.plugin.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IMappingHelperModel extends IModel {
  
  public static final String SELECTED_PROPERTY_COLLECTION_ID = "selectedPropertyCollectionId";
  public static final String TAB_ID                          = "tabId";
  public static final String CONFIG_DETAILS                  = "configDetails";
  public static final String IS_EXPORT                       = "isExport";
  public static final String SELECTED_CONTEXT_ID             = "selectedContextId";
  
  public IMappingConfigDetailsHelperModel getConfigDetails();
  
  public void setConfigDetails(IMappingConfigDetailsHelperModel configDetails);
  
  public String getSelectedPropertyCollectionId();
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public Boolean getIsExport();
  
  public void setIsExport(Boolean isExport);
  
  public String getSelectedContextId();
  
  public void setSelectedContextId(String selectedContextId);
}
