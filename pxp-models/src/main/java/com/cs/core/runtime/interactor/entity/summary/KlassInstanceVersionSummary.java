package com.cs.core.runtime.interactor.entity.summary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassInstanceVersionSummary implements IKlassInstanceVersionSummary {
  
  private static final long           serialVersionUID                = 1L;
  
  protected int                   lifeCycleStatusChanged          = 0;
  protected int                   listStatusChanged               = 0;
  protected int                   attributeChanged;
  protected int                   tagChanged;
  protected int                   relationshipChanged;
  protected int                   natureRelationshipChanged       = 0;
  protected int                   roleChanged;
  protected int                   eventScheduleChanged;
  protected int                   klassAdded;
  protected int                   taxonomyAdded;
  protected int                   klassRemoved;
  protected int                   taxonomyRemoved;
  protected List<String>              attributeIds;
  protected List<String>              tagIds;
  protected List<String>              relationshipIds;
  protected List<String>              natureRelationshipIds;
  protected Boolean                   isDefaultAssetInstanceIdChanged = false;
  protected int                   mamValidityChanged              = 0;
  protected Map<String, List<String>> dependentAttributeIdsMap;
  protected Map<String, Integer>      dependentAttributeIdsCountMap;
  protected int                       languageAdded = 0;
  protected int                       languageRemoved = 0;
  
  @Override
  public int getLifeCycleStatusChanged()
  {
    return lifeCycleStatusChanged;
  }
  
  @Override
  public void setLifeCycleStatusChanged(int lifeCycleStatusChanged)
  {
    this.lifeCycleStatusChanged = lifeCycleStatusChanged;
  }
  
  @Override
  public int getListStatusChanged()
  {
    return listStatusChanged;
  }
  
  @Override
  public void setListStatusChanged(int listStatusChanged)
  {
    this.listStatusChanged = listStatusChanged;
  }
  
  @Override
  public int getAttributeChanged()
  {
    return attributeChanged;
  }
  
  @Override
  public void setAttributeChanged(int attributeChanged)
  {
    this.attributeChanged = attributeChanged;
  }
  
  @Override
  public int getTagChanged()
  {
    return tagChanged;
  }
  
  @Override
  public void setTagChanged(int tagChanged)
  {
    this.tagChanged = tagChanged;
  }
  
  @Override
  public int getRelationshipChanged()
  {
    return relationshipChanged;
  }
  
  @Override
  public void setRelationshipChanged(int relationshipChanged)
  {
    this.relationshipChanged = relationshipChanged;
  }
  
  @Override
  public int getRoleChanged()
  {
    return roleChanged;
  }
  
  @Override
  public void setRoleChanged(int roleChanged)
  {
    this.roleChanged = roleChanged;
  }
  
  @Override
  public int getEventScheduleChanged()
  {
    return eventScheduleChanged;
  }
  
  @Override
  public void setEventScheduleChanged(int eventScheduleChanged)
  {
    this.eventScheduleChanged = eventScheduleChanged;
  }
  
  @Override
  public int getKlassRemoved()
  {
    return klassRemoved;
  }
  
  @Override
  public void setKlassRemoved(int klassRemoved)
  {
    this.klassRemoved = klassRemoved;
  }
  
  @Override
  public int getKlassAdded()
  {
    return klassAdded;
  }
  
  @Override
  public void setKlassAdded(int klassAdded)
  {
    this.klassAdded = klassAdded;
  }
  
  @Override
  public int getTaxonomyAdded()
  {
    return taxonomyAdded;
  }
  
  @Override
  public void setTaxonomyAdded(int taxonomyAdded)
  {
    this.taxonomyAdded = taxonomyAdded;
  }
  
  @Override
  public int getTaxonomyRemoved()
  {
    return taxonomyRemoved;
  }
  
  @Override
  public void setTaxonomyRemoved(int taxonomyRemoved)
  {
    this.taxonomyRemoved = taxonomyRemoved;
  }
  
  @Override
  public int getNatureRelationshipChanged()
  {
    return natureRelationshipChanged;
  }
  
  @Override
  public void setNatureRelationshipChanged(int natureRelationshipChanged)
  {
    this.natureRelationshipChanged = natureRelationshipChanged;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    if (attributeIds == null) {
      attributeIds = new ArrayList<>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
  
  @Override
  public List<String> getTagIds()
  {
    if (tagIds == null) {
      tagIds = new ArrayList<>();
    }
    return tagIds;
  }
  
  @Override
  public void setTagIds(List<String> tagIds)
  {
    this.tagIds = tagIds;
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new ArrayList<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    if (natureRelationshipIds == null) {
      natureRelationshipIds = new ArrayList<>();
    }
    return natureRelationshipIds;
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    this.natureRelationshipIds = natureRelationshipIds;
  }
  
  @Override
  public Boolean getIsDefaultAssetInstanceIdChanged()
  {
    return isDefaultAssetInstanceIdChanged;
  }
  
  @Override
  public void setIsDefaultAssetInstanceIdChanged(Boolean isDefaultAssetInstanceIdChanged)
  {
    this.isDefaultAssetInstanceIdChanged = isDefaultAssetInstanceIdChanged;
  }
  
  @Override
  public int getMamValidityChanged()
  {
    return mamValidityChanged;
  }
  
  @Override
  public void setMamValidityChanged(int mamValidityChanged)
  {
    this.mamValidityChanged = mamValidityChanged;
  }
  
  @Override
  public Map<String, List<String>> getDependentAttributeIdsMap()
  {
    if (dependentAttributeIdsMap == null) {
      dependentAttributeIdsMap = new HashMap<>();
    }
    return dependentAttributeIdsMap;
  }
  
  @Override
  public void setDependentAttributeIdsMap(Map<String, List<String>> dependentAttributeIdsMap)
  {
    this.dependentAttributeIdsMap = dependentAttributeIdsMap;
  }
  
  @Override
  public Map<String, Integer> getDependentAttributeIdsCountMap()
  {
    if (dependentAttributeIdsCountMap == null) {
      dependentAttributeIdsCountMap = new HashMap<>();
    }
    return dependentAttributeIdsCountMap;
  }
  
  @Override
  public void setDependentAttributeIdsCountMap(Map<String, Integer> dependentAttributeIdsCountMap)
  {
    this.dependentAttributeIdsCountMap = dependentAttributeIdsCountMap;
  }
  
  @Override
  public Integer getLanguageAdded()
  {
    return this.languageAdded;
  }
  
  @Override
  public void setLanguageAdded(Integer languageAdded)
  {
    this.languageAdded = languageAdded;
  }
  
  @Override
  public Integer getLanguageRemoved()
  {
    return this.languageRemoved;
  }
  
  @Override
  public void setLanguageRemoved(Integer languageRemoved)
  {
    this.languageRemoved = languageRemoved;
  }
  
  /** ********************* IGNORE FIELDS ************************** */
  @JsonIgnore
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
