package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.IAddedRelationshipInstanceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ISaveKlassInstancePluginModel extends IModel {
  
  public static final String KLASS_INSTANCE                           = "klassInstance";
  public static final String PROPERTY_VERSIONS                        = "propertyVersions";
  public static final String ADDED_RELATIONSHIP_INSTANCES             = "addedRelationshipInstances";
  public static final String DELETED_RELATIONSHIP_INSTANCE_IDS        = "deletedRelationshipInstanceIds";
  public static final String ADDED_NATURE_RELATIONSHIP_INSTANCES      = "addedNatureRelationshipInstances";
  public static final String DELETED_NATURE_RELATIONSHIP_INSTANCE_IDS = "deletedNatureRelationshipInstanceIds";
  public static final String MODIFIED_RELATIONSHIP_INSTANCES          = "modifiedRelationshipInstances";
  public static final String MODIFIED_NATURE_RELATIONSHIP_INSTANCES   = "modifiedNatureRelationshipInstances";
  public static final String EVENT_INSTANCE                           = "eventInstance";
  
  public Map<String, Object> getEventInstance();
  
  public void setEventInstance(Map<String, Object> eventInstance);
  
  public Map<String, Object> getKlassInstance();
  
  public void setKlassInstance(Map<String, Object> klassInstance);
  
  public List<HashMap<String, Object>> getPropertyVersions();
  
  public void setPropertyVersions(List<HashMap<String, Object>> propertyVersions);
  
  public List<IAddedRelationshipInstanceModel> getAddedRelationshipInstances();
  
  public void setAddedRelationshipInstances(
      List<IAddedRelationshipInstanceModel> relationshipInstances);
  
  public List<String> getDeletedRelationshipInstanceIds();
  
  public void setDeletedRelationshipInstanceIds(List<String> deletedRelationshipInstanceIds);
  
  public List<IAddedRelationshipInstanceModel> getAddedNatureRelationshipInstances();
  
  public void setAddedNatureRelationshipInstances(
      List<IAddedRelationshipInstanceModel> addedNatureRelationshipInstances);
  
  public List<String> getDeletedNatureRelationshipInstanceIds();
  
  public void setDeletedNatureRelationshipInstanceIds(
      List<String> deletedNatureRelationshipInstanceIds);
  
  public List<Map<String, Object>> getModifiedRelationshipInstances();
  
  public void setModifiedRelationshipInstances(List<Map<String, Object>> relationshipInstances);
  
  public List<Map<String, Object>> getModifiedNatureRelationshipInstances();
  
  public void setModifiedNatureRelationshipInstances(
      List<Map<String, Object>> relationshipInstances);
}
