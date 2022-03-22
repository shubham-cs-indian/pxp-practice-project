package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetInstanceRequestModel extends IModel {
  
  public static final String ID                     = "id";
  public static final String MODULE_ID              = "moduleId";
  public static final String CHILD_CONTEXT_ID       = "childContextId";
  public static final String TEMPLATE_ID            = "templateId";
  public static final String FROM                   = "from";
  public static final String SIZE                   = "size";
  public static final String SORT_FIELD             = "sortField";
  public static final String SORT_ORDER             = "sortOrder";
  public static final String VARIANT_INSTANCE_IDS   = "variantInstanceIds";
  public static final String VARIANT_INSTANCE_ID    = "variantInstanceId";
  public static final String IS_GET_ALL             = "isGetAll";
  public static final String TAB_TYPE               = "tabType";
  public static final String CONTEXT_ID             = "contextId";
  public static final String PARENT_CONTEXT_ID      = "parentContextId";
  public static final String IS_COMPARE             = "isCompare";
  public static final String IS_FOR_TASK_ANNOTATION = "isForTaskAnnotation";
  public static final String SELECTED_TIME_RANGE    = "selectedTimeRange";
  public static final String TAB_ID                 = "tabId";
  public static final String TYPE_ID                = "typeId";
  public static final String ROLE_ID                = "roleId";
  public static final String LANGUAGE_CODE          = "languageCode";
  
  public String getId();
  
  public void setId(String id);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
  
  public String getChildContextId();
  
  public void setChildContextId(String childContextId);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getSortField();
  
  public void setSortField(String sortField);
  
  public String getSortOrder();
  
  public void setSortOrder(String sortOrder);
  
  public List<String> getVariantInstanceIds();
  
  public void setVariantInstanceIds(List<String> variantInstanceIds);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public Boolean getIsGetAll();
  
  public void setIsGetAll(Boolean isGetAll);
  
  public String getTabType();
  
  public void setTabType(String tabType);
  
  public Boolean getHasReadPermission();
  
  void setHasReadPermission(Boolean hasReadPermission);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getParentContextId();
  
  public void setParentContextId(String parentContextId);
  
  public Boolean getIsCompare();
  
  public void setIsCompare(Boolean isCompare);
  
  public Boolean getIsForTaskAnnotation();
  
  public void setIsForTaskAnnotation(Boolean isForTaskAnnotation);
  
  public ITimeRange getSelectedTimeRange();
  
  public void setSelectedTimeRange(ITimeRange selectedTimeRange);
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
}
