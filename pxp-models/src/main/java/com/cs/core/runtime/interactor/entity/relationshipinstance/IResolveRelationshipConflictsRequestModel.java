package com.cs.core.runtime.interactor.entity.relationshipinstance;


public interface IResolveRelationshipConflictsRequestModel extends IInitiateRelationshipInheritanceRequestModel{
  
  String TAB_ID      = "tabId";
  String TAB_TYPE    = "tabType";
  String TEMPLATE_ID = "templateId";
  String TYPE_ID     = "typeId";
  
  public String getTabId();
  public void setTabId(String tabId);
  
  public String getTabType();
  public void setTabType(String tabType);
  
  public String getTemplateId();
  public void setTemplateId(String templateId);
  
  public String getTypeId();
  public void setTypeId(String typeId);
}
