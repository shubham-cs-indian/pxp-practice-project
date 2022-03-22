package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetOutAndInboundMappingModel extends IModel {
 
  String SELECTED_PROPERTY_COLLECTION_ID = "selectedPropertyCollectionId";
  String TAB                             = "tabId";
  String MAPPING_ID                      = "id";
  String SELECTED_CONTEXT_ID             = "selectedContextId";
  
  public String getSelectedPropertyCollectionId();
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getId();
  
  public void setId(String id);

  public String getSelectedContextId();

  public void setSelectedContextId(String selectedContextId);
}
