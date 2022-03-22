package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class KlassInstanceTypeSwitchModel implements IKlassInstanceTypeSwitchModel {
  
  private static final long                    serialVersionUID         = 1L;
  
  protected String                             klassInstanceId;
  protected List<String>                       deletedMultiClassificationTypes;
  protected String                             addedMultiClassificationTypes;
  protected IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeInfo = null;
  protected String                             addedTaxonomyId;
  protected List<String>                       deletedTaxonomyIds;
  protected String                             contextId;
  protected Boolean                            isNatureKlassSwitched    = false;
  protected String                             natureClassId;
  protected String                             templateId;
  protected String                             tabType;
  protected Boolean                            isGetAll                 = false;
  protected String                             componentId;
  protected String                             tabId;
  protected String                             typeId;
  protected Boolean                            isMinorTaxonomySwitch;
  protected String                             processInstanceId;
  protected List<String>                       addedSecondaryTypes;
  protected List<String>                       addedTaxonomyIds;
  protected Boolean                            shouldCheckPermission    = true;
  protected Boolean                            isResolved    = false;
  protected boolean                            triggerAfterSaveEvent = true;
  
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
  public String getKlassInstanceId()
  {
    return this.klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public IGetKlassInstanceTreeStrategyModel getGetKlassInstanceTreeInfo()
  {
    return getKlassInstanceTreeInfo;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  @Override
  public void setGetKlassInstanceTreeInfo(
      IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeInfo)
  {
    this.getKlassInstanceTreeInfo = getKlassInstanceTreeInfo;
  }
  
  @Override
  public String getAddedSecondaryType()
  {
    return addedMultiClassificationTypes;
  }
  
  @Override
  public void setAddedSecondaryType(String addedMultiClassificationTypes)
  {
    this.addedMultiClassificationTypes = addedMultiClassificationTypes;
  }
  
  @Override
  public String getAddedTaxonomyId()
  {
    return addedTaxonomyId;
  }
  
  @Override
  public void setAddedTaxonomyId(String addedTaxonomyId)
  {
    this.addedTaxonomyId = addedTaxonomyId;
  }
  
  @Override
  public String getNatureClassId()
  {
    return natureClassId;
  }
  
  @Override
  public void setNatureClassId(String natureKlassId)
  {
    this.natureClassId = natureKlassId;
  }
  
  @Override
  public Boolean getIsNatureKlassSwitched()
  {
    return isNatureKlassSwitched;
  }
  
  @Override
  public void setIsNatureKlassSwitched(Boolean isNatureKlassSwitched)
  {
    this.isNatureKlassSwitched = isNatureKlassSwitched;
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
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
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
  public Boolean getIsMinorTaxonomySwitch()
  {
    return isMinorTaxonomySwitch;
  }
  
  @Override
  public void setIsMinorTaxonomySwitch(Boolean isMinorTaxonomySwitch)
  {
    this.isMinorTaxonomySwitch = isMinorTaxonomySwitch;
  }
  
  @Override
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public List<String> getAddedSecondaryTypes()
  {
    if (addedSecondaryTypes == null) {
      addedSecondaryTypes = new ArrayList<>();
      if (addedMultiClassificationTypes != null) {
        addedSecondaryTypes.add(addedMultiClassificationTypes);
      }
    }
    return addedSecondaryTypes;
  }
  
  @Override
  public void setAddedSecondaryTypes(List<String> addedSecondaryTypes)
  {
    this.addedSecondaryTypes = addedSecondaryTypes;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    if (addedTaxonomyIds == null) {
      addedTaxonomyIds = new ArrayList<>();
      if (addedTaxonomyId != null) {
        addedTaxonomyIds.add(addedTaxonomyId);
      }
    }
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public Boolean getShouldCheckPermission()
  {
    return shouldCheckPermission;
  }
  
  @Override
  public void setShouldCheckPermission(Boolean shouldCheckPermission)
  {
    this.shouldCheckPermission = shouldCheckPermission;
  }
  
  @Override
  public List<String> getDeletedSecondaryTypes()
  {
    if (deletedMultiClassificationTypes == null) {
      return new ArrayList<>();
    }
    return deletedMultiClassificationTypes;
  }
  
  @Override
  public void setDeletedSecondaryTypes(List<String> deletedMultiClassificationTypes)
  {
    this.deletedMultiClassificationTypes = deletedMultiClassificationTypes;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    if (deletedTaxonomyIds == null) {
      return new ArrayList<>();
    }
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }

  @Override
  public Boolean getIsResolved()
  {
    return isResolved;
  }

  @Override
  public void setIsResolved(Boolean isResolved)
  {
    this.isResolved = isResolved;
  }
  
  public boolean getTriggerEvent()
  {
    return this.triggerAfterSaveEvent;
  }

  @Override
  public void setTriggerEvent(boolean triggerAfterSaveEvent)
  {
    this.triggerAfterSaveEvent = triggerAfterSaveEvent;
    
  }
}
