package com.cs.core.runtime.interactor.model.relationship;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.instance.ContentRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IContentRelationshipInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveRelationshipInstanceModel implements ISaveRelationshipInstanceModel {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  id;
  protected String                                  baseType;
  protected List<IContentRelationshipInstanceModel> addedRelationships;
  protected List<IContentRelationshipInstanceModel> deletedRelationships;
  protected List<IContentRelationshipInstanceModel> modifiedRelationships;
  protected List<IContentRelationshipInstanceModel> addedNatureRelationships;
  protected List<IContentRelationshipInstanceModel> deletedNatureRelationships;
  protected List<IContentRelationshipInstanceModel> modifiedNatureRelationships;
  protected String                                  templateId;
  protected String                                  tabType;
  protected String                                  tabId;
  protected String                                  typeId;
  protected boolean                                 triggerAfterSaveEvent = true;
  protected Boolean                                 isManuallyCreated = false;
  
  @Override
  public List<IContentRelationshipInstanceModel> getAddedRelationships()
  {
    if (addedRelationships == null) {
      addedRelationships = new ArrayList<>();
    }
    return addedRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstanceModel.class)
  @Override
  public void setAddedRelationships(List<IContentRelationshipInstanceModel> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstanceModel> getDeletedRelationships()
  {
    if (deletedRelationships == null) {
      deletedRelationships = new ArrayList<>();
    }
    return deletedRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstanceModel.class)
  @Override
  public void setDeletedRelationships(List<IContentRelationshipInstanceModel> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstanceModel> getModifiedRelationships()
  {
    if (modifiedRelationships == null) {
      modifiedRelationships = new ArrayList<>();
    }
    return modifiedRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstanceModel.class)
  @Override
  public void setModifiedRelationships(
      List<IContentRelationshipInstanceModel> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstanceModel> getAddedNatureRelationships()
  {
    if (addedNatureRelationships == null) {
      addedNatureRelationships = new ArrayList<>();
    }
    return addedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstanceModel.class)
  @Override
  public void setAddedNatureRelationships(
      List<IContentRelationshipInstanceModel> addedNatureRelationships)
  {
    this.addedNatureRelationships = addedNatureRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstanceModel> getDeletedNatureRelationships()
  {
    if (deletedNatureRelationships == null) {
      deletedNatureRelationships = new ArrayList<>();
    }
    return deletedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstanceModel.class)
  @Override
  public void setDeletedNatureRelationships(
      List<IContentRelationshipInstanceModel> deletedNatureRelationships)
  {
    this.deletedNatureRelationships = deletedNatureRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstanceModel> getModifiedNatureRelationships()
  {
    if (modifiedNatureRelationships == null) {
      modifiedNatureRelationships = new ArrayList<>();
    }
    return modifiedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstanceModel.class)
  @Override
  public void setModifiedNatureRelationships(
      List<IContentRelationshipInstanceModel> modifiedNatureRelationships)
  {
    this.modifiedNatureRelationships = modifiedNatureRelationships;
  }
  
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
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
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
  

  public boolean getTriggerEvent()
  {
    return this.triggerAfterSaveEvent;
  }

  @Override
  public void setTriggerEvent(boolean triggerEvent)
  {
    this.triggerAfterSaveEvent = triggerEvent;
  }
  
  @Override
  public Boolean getIsManuallyCreated()
  {
    return isManuallyCreated;
  }

  @Override
  public void setIsManuallyCreated(Boolean isManuallyCreated)
  {
    this.isManuallyCreated = isManuallyCreated;
  }

}
