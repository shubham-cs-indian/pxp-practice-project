package com.cs.core.runtime.interactor.entity.summary;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import java.util.List;
import java.util.Map;

public interface IKlassInstanceVersionSummary extends IEntity {
  
  public static final String LIFE_CYCLE_STATUS_CHANGED            = "lifeCycleStatusChanged";
  public static final String LIST_STATUS_CHANGED                  = "listStatusChanged";
  public static final String ROLE_CHANGED                         = "roleChanged";
  public static final String RELATIONSHIP_CHANGED                 = "relationshipChanged";
  public static final String NATURE_RELATIONSHIP_CHANGED          = "natureRelationshipChanged";
  public static final String TAG_CHANGED                          = "tagChanged";
  public static final String ATTRIBUTE_CHANGED                    = "attributeChanged";
  public static final String EVENT_SCHEDULE_CHANGED               = "eventScheduleChanged";
  public static final String KLASS_ADDED                          = "klassAdded";
  public static final String TAXONOMY_ADDED                       = "taxonomyAdded";
  public static final String KLASS_REMOVED                        = "klassRemoved";
  public static final String TAXONOMY_REMOVED                     = "taxonomyRemoved";
  public static final String ATTRIBUTE_IDS                        = "attributeIds";
  public static final String TAG_IDS                              = "tagIds";
  public static final String RELATIONSHIP_IDS                     = "relationshipIds";
  public static final String NATURE_RELATIONSHIP_IDS              = "natureRelationshipIds";
  public static final String IS_DEFAULT_ASSET_INSTANCE_ID_CHANGED = "isDefaultAssetInstanceIdChanged";
  public static final String MAM_VALIDITY_CHANGED                 = "mamValidityChanged";
  public static final String DEPENDENT_ATTRIBUTE_IDS_MAP          = "dependentAttributeIdsMap";
  public static final String DEPENDENT_ATTRIBUTE_IDS_COUNT_MAP    = "dependentAttributeIdsCountMap";
  public static final String LANGUAGE_ADDED                       = "languageAdded";
  public static final String LANGUAGE_REMOVED                     = "languageRemoved";
  
  public int getLifeCycleStatusChanged();
  
  public void setLifeCycleStatusChanged(int lifeCycleStatusChanged);
  
  public int getListStatusChanged();
  
  public void setListStatusChanged(int listStatusChanged);
  
  public int getAttributeChanged();
  
  public void setAttributeChanged(int attributeChanged);
  
  public int getTagChanged();
  
  public void setTagChanged(int tagChanged);
  
  public int getRelationshipChanged();
  
  public void setRelationshipChanged(int relationshipChanged);
  
  public int getRoleChanged();
  
  public void setRoleChanged(int roleChanged);
  
  public int getEventScheduleChanged();
  
  public void setEventScheduleChanged(int eventScheduleChanged);
  
  public int getKlassRemoved();
  
  public void setKlassRemoved(int klassRemoved);
  
  public int getKlassAdded();
  
  public void setKlassAdded(int klassAdded);
  
  public int getTaxonomyAdded();
  
  public void setTaxonomyAdded(int taxonomyAdded);
  
  public int getTaxonomyRemoved();
  
  public void setTaxonomyRemoved(int taxonomyRemoved);
  
  public int getNatureRelationshipChanged();
  
  public void setNatureRelationshipChanged(int natureRelationshipChanged);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public List<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(List<String> natureRelationshipIds);
  
  public Boolean getIsDefaultAssetInstanceIdChanged();
  
  public void setIsDefaultAssetInstanceIdChanged(Boolean isDefaultAssetInstanceIdChanged);
  
  public int getMamValidityChanged();
  
  public void setMamValidityChanged(int mamValidityChanged);
  
  public Map<String, List<String>> getDependentAttributeIdsMap();
  
  public void setDependentAttributeIdsMap(Map<String, List<String>> dependentAttributeIdsMap);
  
  public Map<String, Integer> getDependentAttributeIdsCountMap();
  
  public void setDependentAttributeIdsCountMap(Map<String, Integer> dependentAttributeIdsCountMap);
  
  void setLanguageAdded(Integer languageAdded);
  Integer getLanguageAdded();
  
  void setLanguageRemoved(Integer languageRemoved);
  Integer getLanguageRemoved();
}
