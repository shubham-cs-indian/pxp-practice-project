package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetInstanceRequestModel implements IGetInstanceRequestModel {
  
  private static final long serialVersionUID    = 1L;
  
  protected String          id;
  protected String          moduleId;
  protected String          templateId;
  protected String          childContextId;
  protected String          sortField;
  protected String          sortOrder;
  protected Integer         from;
  protected Integer         size;
  
  /* only used for contextual/lang tab */
  protected String          variantInstanceId;
  protected List<String>    variantInstanceIds  = new ArrayList<String>();
  protected Boolean         isGetAll            = false;
  protected String          tabType;
  protected Boolean         hasReadPermission;
  protected String          contextId;
  protected String          parentContextId;
  protected Boolean         isCompare           = false;
  protected Boolean         isForTaskAnnotation = false;
  protected ITimeRange      selectedTimeRange;
  protected String          tabId;
  protected String          typeId;
  protected String          roleId;
  protected String          languageCode;
  
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
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
  
  @Override
  public String getChildContextId()
  {
    return childContextId;
  }
  
  @Override
  public void setChildContextId(String childContextId)
  {
    this.childContextId = childContextId;
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
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public String getSortField()
  {
    return sortField;
  }
  
  @Override
  public void setSortField(String sortField)
  {
    this.sortField = sortField;
  }
  
  @Override
  public String getSortOrder()
  {
    return sortOrder;
  }
  
  @Override
  public void setSortOrder(String sortOrder)
  {
    this.sortOrder = sortOrder;
  }
  
  @Override
  public List<String> getVariantInstanceIds()
  {
    return variantInstanceIds;
  }
  
  @Override
  public void setVariantInstanceIds(List<String> variantInstanceIds)
  {
    this.variantInstanceIds = variantInstanceIds;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
  
  @Override
  public Boolean getIsGetAll()
  {
    return isGetAll;
  }
  
  @Override
  public void setIsGetAll(Boolean isGetAll)
  {
    this.isGetAll = isGetAll;
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
  public Boolean getHasReadPermission()
  {
    return hasReadPermission;
  }
  
  @Override
  public void setHasReadPermission(Boolean hasReadPermission)
  {
    this.hasReadPermission = hasReadPermission;
  }
  
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getParentContextId()
  {
    return parentContextId;
  }
  
  @Override
  public void setParentContextId(String parentContextId)
  {
    this.parentContextId = parentContextId;
  }
  
  @Override
  public Boolean getIsCompare()
  {
    return isCompare;
  }
  
  @Override
  public void setIsCompare(Boolean isCompare)
  {
    this.isCompare = isCompare;
  }
  
  public Boolean getIsForTaskAnnotation()
  {
    return isForTaskAnnotation;
  }
  
  @Override
  public void setIsForTaskAnnotation(Boolean isForTaskAnnotation)
  {
    this.isForTaskAnnotation = isForTaskAnnotation;
  }
  
  @Override
  public ITimeRange getSelectedTimeRange()
  {
    return selectedTimeRange;
  }
  
  @Override
  @JsonDeserialize(as = TimeRange.class)
  public void setSelectedTimeRange(ITimeRange selectedTimeRange)
  {
    this.selectedTimeRange = selectedTimeRange;
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
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }
}
