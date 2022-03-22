package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IKlassInstanceVersionSummaryModel extends IModel {
  
  public static final String LIFE_CYCLE_STATUS_CHANGED                  = "lifeCycleStatusChanged";
  public static final String LIST_STATUS_CHANGED                        = "listStatusChanged";
  public static final String ROLE_CHANGED                               = "roleChanged";
  public static final String RELATIONSHIP_CHANGED                       = "relationshipChanged";
  public static final String NATURE_RELATIONSHIP_CHANGED                = "natureRelationshipChanged";
  public static final String TAG_CHANGED                                = "tagChanged";
  public static final String ATTRIBUTE_CHANGED                          = "attributeChanged";
  public static final String EVENT_SCHEDULE_CHANGED                     = "eventScheduleChanged";
  public static final String KLASS_ADDED                                = "klassAdded";
  public static final String TAXONOMY_ADDED                             = "taxonomyAdded";
  public static final String KLASS_REMOVED                              = "klassRemoved";
  public static final String TAXONOMY_REMOVED                           = "taxonomyRemoved";
  public static final String IS_DEFAULT_ASSET_INSTANCE_ID_CHANGED       = "isDefaultAssetInstanceIdChanged";
  public static final String MAM_VALIDITY_CHANGED                       = "mamValidityChanged";
  public static final String DEPENDENT_ATTRIBUTE_INSTANCE_IDS_COUNT_MAP = "dependentAttributeInstanceIdsCountMap";
  public static final String LANGUAGE_ADDED                             = "languageAdded";
  public static final String LANGUAGE_REMOVED                           = "languageRemoved";
  
  public Integer getLifeCycleStatusChanged();
  
  public void setLifeCycleStatusChanged(Integer lifeCycleStatusChanged);
  
  public Integer getListStatusChanged();
  
  public void setListStatusChanged(Integer listStatusChanged);
  
  public Integer getAttributeChanged();
  
  public void setAttributeChanged(Integer attributeChanged);
  
  public Integer getTagChanged();
  
  public void setTagChanged(Integer tagChanged);
  
  public Integer getRelationshipChanged();
  
  public void setRelationshipChanged(Integer relationshipChanged);
  
  public Integer getRoleChanged();
  
  public void setRoleChanged(Integer roleChanged);
  
  public Integer getEventScheduleChanged();
  
  public void setEventScheduleChanged(Integer eventScheduleChanged);
  
  public Integer getKlassRemoved();
  
  public void setKlassRemoved(Integer klassRemoved);
  
  public Integer getKlassAdded();
  
  public void setKlassAdded(Integer klassAdded);
  
  public Integer getTaxonomyAdded();
  
  public void setTaxonomyAdded(Integer taxonomyAdded);
  
  public Integer getTaxonomyRemoved();
  
  public void setTaxonomyRemoved(Integer taxonomyRemoved);
  
  public Integer getNatureRelationshipChanged();
  
  public void setNatureRelationshipChanged(Integer natureRelationshipChanged);
  
  public Boolean getIsDefaultAssetInstanceIdChanged();
  
  public void setIsDefaultAssetInstanceIdChanged(Boolean isDefaultAssetInstanceIdChanged);
  
  public Integer getMamValidityChanged();
  
  public void setMamValidityChanged(Integer mamValidityChanged);
  
  public Map<String, Integer> getDependentAttributeIdsCountMap();
  
  public void setDependentAttributeIdsCountMap(
      Map<String, Integer> dependentAttributeInstanceIdsCountMap);
  
  void setLanguageAdded(Integer languageAdded);
  Integer getLanguageAdded();
  
  void setLanguageRemoved(Integer languageRemoved);
  Integer getLanguageRemoved();
}
