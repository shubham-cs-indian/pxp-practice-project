package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.datarule.IAddedRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.instance.AddedRelationshipInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveKlassInstancePluginModel implements ISaveKlassInstancePluginModel {
  
  private static final long                       serialVersionUID = 1L;
  protected Map<String, Object>                   klassInstance;
  protected List<HashMap<String, Object>>         propertyVersions;
  protected List<IAddedRelationshipInstanceModel> addedRelationshipInstances;
  protected List<String>                          deletedRelationshipInstanceIds;
  protected List<IAddedRelationshipInstanceModel> addedNatureRelationshipInstances;
  protected List<String>                          deletedNatureRelationshipInstanceIds;
  protected List<Map<String, Object>>             modifiedRelationshipInstances;
  protected List<Map<String, Object>>             modifiedNatureRelationshipInstances;
  protected Map<String, Object>                   eventInstance;
  
  @Override
  public Map<String, Object> getEventInstance()
  {
    return eventInstance;
  }
  
  @JsonDeserialize(as = HashMap.class)
  @Override
  public void setEventInstance(Map<String, Object> eventInstance)
  {
    this.eventInstance = eventInstance;
  }
  
  @Override
  public Map<String, Object> getKlassInstance()
  {
    return klassInstance;
  }
  
  @JsonDeserialize(as = HashMap.class)
  @Override
  public void setKlassInstance(Map<String, Object> klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public List<HashMap<String, Object>> getPropertyVersions()
  {
    return propertyVersions;
  }
  
  @JsonDeserialize(as = ArrayList.class)
  @Override
  public void setPropertyVersions(List<HashMap<String, Object>> propertyVersions)
  {
    this.propertyVersions = propertyVersions;
  }
  
  @Override
  public List<IAddedRelationshipInstanceModel> getAddedRelationshipInstances()
  {
    return addedRelationshipInstances;
  }
  
  @JsonDeserialize(contentAs = AddedRelationshipInstanceModel.class)
  @Override
  public void setAddedRelationshipInstances(
      List<IAddedRelationshipInstanceModel> relationshipInstances)
  {
    this.addedRelationshipInstances = relationshipInstances;
  }
  
  @Override
  public List<String> getDeletedRelationshipInstanceIds()
  {
    return deletedRelationshipInstanceIds;
  }
  
  @JsonDeserialize(as = ArrayList.class)
  @Override
  public void setDeletedRelationshipInstanceIds(List<String> deletedRelationshipInstanceIds)
  {
    this.deletedRelationshipInstanceIds = deletedRelationshipInstanceIds;
  }
  
  @Override
  public List<IAddedRelationshipInstanceModel> getAddedNatureRelationshipInstances()
  {
    
    return addedNatureRelationshipInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = AddedRelationshipInstanceModel.class)
  public void setAddedNatureRelationshipInstances(
      List<IAddedRelationshipInstanceModel> addedNatureRelationshipInstances)
  {
    this.addedNatureRelationshipInstances = addedNatureRelationshipInstances;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipInstanceIds()
  {
    
    return deletedNatureRelationshipInstanceIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipInstanceIds(
      List<String> deletedNatureRelationshipInstanceIds)
  {
    this.deletedNatureRelationshipInstanceIds = deletedNatureRelationshipInstanceIds;
  }
  
  @Override
  public List<Map<String, Object>> getModifiedRelationshipInstances()
  {
    return modifiedRelationshipInstances;
  }
  
  @Override
  public void setModifiedRelationshipInstances(List<Map<String, Object>> relationshipInstances)
  {
    this.modifiedRelationshipInstances = relationshipInstances;
  }
  
  @Override
  public List<Map<String, Object>> getModifiedNatureRelationshipInstances()
  {
    return modifiedNatureRelationshipInstances;
  }
  
  @Override
  public void setModifiedNatureRelationshipInstances(
      List<Map<String, Object>> relationshipInstances)
  {
    this.modifiedNatureRelationshipInstances = relationshipInstances;
  }
}
