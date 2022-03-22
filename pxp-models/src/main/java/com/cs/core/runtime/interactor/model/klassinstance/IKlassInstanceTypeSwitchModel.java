package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassInstanceTypeSwitchModel extends IModel {
  
  public static final String KLASS_INSTANCE_ID            = "klassInstanceId";
  public static final String GET_KLASS_INSTANCE_TREE_INFO = "getKlassInstanceTreeInfo";
  public static final String DELETED_SECONDARY_TYPE       = "deletedSecondaryType";
  public static final String ADDED_SECONDARY_TYPE         = "addedSecondaryType";
  public static final String ADDED_TAXONOMY_ID            = "addedTaxonomyId";
  public static final String DELETED_TAXONOMY_ID          = "deletedTaxonomyId";
  public static final String CONTEXT_ID                   = "contextId";
  public static final String NATURE_KLASS_ID              = "natureKlassId";
  public static final String IS_NATURE_KLASS_SWITCHED     = "isNatureKlassSwitched";
  public static final String TAB_TYPE                     = "tabType";
  public static final String TEMPLATE_ID                  = "templateId";
  public static final String IS_GET_ALL                   = "isGetAll";
  public static final String TAB_ID                       = "tabId";
  public static final String TYPE_ID                      = "typeId";
  public static final String IS_MINOR_TAXONOMY_SWITCH     = "isMinorTaxonomySwitch";
  public static final String PROCESS_INSTANCE_ID          = "processInstanceId";
  public static final String ADDED_SECONDARY_TYPES        = "addedSecondaryTypes";
  public static final String ADDED_TAXONOMY_IDS           = "addedTaxonomyIds";
  public static final String SHOULD_CHECK_PERMISSION      = "shouldCheckPermission";
  public static final String IS_RESOLVED                  = "isResolved";
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public IGetKlassInstanceTreeStrategyModel getGetKlassInstanceTreeInfo();
  
  public void setGetKlassInstanceTreeInfo(
      IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeInfo);
  
  public String getAddedSecondaryType();
  
  public void setAddedSecondaryType(String addedMultiClassificationTypes);
  
  public List<String> getDeletedSecondaryTypes();
  
  public void setDeletedSecondaryTypes(List<String> deletedMultiClassificationTypes);
  
  public String getAddedTaxonomyId();
  
  public void setAddedTaxonomyId(String addedTaxonomyId);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public String getNatureClassId();
  
  public void setNatureClassId(String natureKlassId);
  
  public Boolean getIsNatureKlassSwitched();
  
  public void setIsNatureKlassSwitched(Boolean isNatureKlassSwitched);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getTabType();
  
  public void setTabType(String tabType);
  
  public Boolean getIsGetAll();
  
  public void setIsGetAll(Boolean isGetAll);
  
  public String getComponentId();
  
  public void setComponentId(String isGetAll);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public Boolean getIsMinorTaxonomySwitch();
  
  public void setIsMinorTaxonomySwitch(Boolean isMinorTaxonomySwitch);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public List<String> getAddedSecondaryTypes();
  
  public void setAddedSecondaryTypes(List<String> addedSecondaryTypes);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public Boolean getShouldCheckPermission();
  
  public void setShouldCheckPermission(Boolean shouldCheckPermission);
  
  public Boolean getIsResolved();
  
  public void setIsResolved(Boolean isResolved);

  public boolean getTriggerEvent();
  
  public void setTriggerEvent(boolean triggerEvent);
}
