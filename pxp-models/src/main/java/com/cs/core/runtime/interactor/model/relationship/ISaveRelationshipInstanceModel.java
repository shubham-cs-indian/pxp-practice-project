package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;

import java.util.List;

public interface ISaveRelationshipInstanceModel extends IModel {
  
  public static final String ID                            = "id";
  public static final String BASETYPE                      = "baseType";
  public static final String ADDED_RELATIONSHIPS           = "addedRelationships";
  public static final String DELETED_RELATIONSHIPS         = "deletedRelationships";
  public static final String MODIFIED_RELATIONSHIPS        = "modifiedRelationships";
  public static final String ADDED_NATURE_RELATIONSHIPS    = "addedNatureRelationships";
  public static final String DELETED_NATURE_RELATIONSHIPS  = "deletedNatureRelationships";
  public static final String MODIFIED_NATURE_RELATIONSHIPS = "modifiedNatureRelationships";
  public static final String TAB_ID                        = "tabId";
  public static final String TYPE_ID                       = "typeId";
  public static final String TEMPLATE_ID                   = "templateId";
  public static final String TAB_TYPE                      = "tabType";
  public static final String TRIGGER_EVENT                 = "triggerEvent";  
  public static final String IS_MANUALLY_CREATED           = "isManuallyCreated";
  
  public List<IContentRelationshipInstanceModel> getAddedRelationships();
  
  public void setAddedRelationships(List<IContentRelationshipInstanceModel> addedRelationships);
  
  public List<IContentRelationshipInstanceModel> getDeletedRelationships();
  
  public void setDeletedRelationships(List<IContentRelationshipInstanceModel> deletedRelationships);
  
  public List<IContentRelationshipInstanceModel> getModifiedRelationships();
  
  public void setModifiedRelationships(
      List<IContentRelationshipInstanceModel> modifiedRelationships);
  
  public List<IContentRelationshipInstanceModel> getAddedNatureRelationships();
  
  public void setAddedNatureRelationships(
      List<IContentRelationshipInstanceModel> addedNatureRelationships);
  
  public List<IContentRelationshipInstanceModel> getDeletedNatureRelationships();
  
  public void setDeletedNatureRelationships(
      List<IContentRelationshipInstanceModel> deletedNatureRelationships);
  
  public List<IContentRelationshipInstanceModel> getModifiedNatureRelationships();
  
  public void setModifiedNatureRelationships(
      List<IContentRelationshipInstanceModel> modifiedRelationships);
  
  public String getId();
  
  public void setId(String id);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getTabType();
  
  public void setTabType(String tabType);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getTypeId();
  
  public void setTypeId(String typeId);

  public boolean getTriggerEvent();
  
  public void setTriggerEvent(boolean triggerEvent);
  
  public Boolean getIsManuallyCreated();
  public void setIsManuallyCreated(Boolean isManuallyCreated);

}
