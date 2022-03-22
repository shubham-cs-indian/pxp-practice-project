package com.cs.core.runtime.interactor.entity.relationshipinstance;

public class ResolveRelationshipConflictsRequestModel extends InitiateRelationshipInheritanceRequestModel
    implements IResolveRelationshipConflictsRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          tabId;
  protected String          tabType;
  protected String          templateId;
  protected String          typeId;
  
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
  public String getTabType()
  {
    return tabType;
  }
  
  @Override
  public void setTabType(String tabType)
  {
    this.tabType = tabType;
  }
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public String getTypeId()
  {
    return typeId;
  }
  
  @Override
  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }
  
}
