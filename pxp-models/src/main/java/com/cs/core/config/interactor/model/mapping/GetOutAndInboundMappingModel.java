package com.cs.core.config.interactor.model.mapping;

public class GetOutAndInboundMappingModel implements IGetOutAndInboundMappingModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          selectedPropertyCollectionId;
  
  protected String          tabId;
  
  protected String          id;
  
  protected String          selectedContextId;
  
  public String getSelectedPropertyCollectionId()
  {
    return selectedPropertyCollectionId;
  }
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId)
  {
    this.selectedPropertyCollectionId = selectedPropertyCollectionId;
  }
  
  public String getTabId()
  {
    return tabId;
  }
  
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getSelectedContextId() {
    return selectedContextId;
  }
  
  public void setSelectedContextId(String selectedContextId) {
    this.selectedContextId = selectedContextId;
  }

}
