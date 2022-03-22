package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.entity.summary.IKlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.summary.KlassInstanceVersionSummary;

import java.util.Map;

public class KlassInstanceVersionSummaryModel implements IKlassInstanceVersionSummaryModel {
  
  private static final long              serialVersionUID = 1L;
  protected IKlassInstanceVersionSummary entity;
  
  public KlassInstanceVersionSummaryModel()
  {
    entity = new KlassInstanceVersionSummary();
  }
  
  @Override
  public Integer getLifeCycleStatusChanged()
  {
    return entity.getLifeCycleStatusChanged();
  }
  
  @Override
  public void setLifeCycleStatusChanged(Integer lifeCycleStatusChanged)
  {
    entity.setLifeCycleStatusChanged(lifeCycleStatusChanged);
  }
  
  @Override
  public Integer getListStatusChanged()
  {
    return entity.getListStatusChanged();
  }
  
  @Override
  public void setListStatusChanged(Integer listStatusChanged)
  {
    entity.setListStatusChanged(listStatusChanged);
  }
  
  @Override
  public Integer getAttributeChanged()
  {
    return entity.getAttributeChanged();
  }
  
  @Override
  public void setAttributeChanged(Integer attributeChanged)
  {
    entity.setAttributeChanged(attributeChanged);
  }
  
  @Override
  public Integer getTagChanged()
  {
    return entity.getTagChanged();
  }
  
  @Override
  public void setTagChanged(Integer tagChanged)
  {
    entity.setTagChanged(tagChanged);
  }
  
  @Override
  public Integer getRelationshipChanged()
  {
    return entity.getRelationshipChanged();
  }
  
  @Override
  public void setRelationshipChanged(Integer relationshipChanged)
  {
    entity.setRelationshipChanged(relationshipChanged);
  }
  
  @Override
  public Integer getRoleChanged()
  {
    return entity.getRoleChanged();
  }
  
  @Override
  public void setRoleChanged(Integer roleChanged)
  {
    entity.setRoleChanged(roleChanged);
  }
  
  @Override
  public Integer getEventScheduleChanged()
  {
    return entity.getEventScheduleChanged();
  }
  
  @Override
  public void setEventScheduleChanged(Integer eventScheduleChanged)
  {
    entity.setEventScheduleChanged(eventScheduleChanged);
  }
  
  @Override
  public Integer getKlassRemoved()
  {
    return entity.getKlassRemoved();
  }
  
  @Override
  public void setKlassRemoved(Integer klassRemoved)
  {
    entity.setKlassRemoved(klassRemoved);
  }
  
  @Override
  public Integer getKlassAdded()
  {
    return entity.getKlassAdded();
  }
  
  @Override
  public void setKlassAdded(Integer klassAdded)
  {
    entity.setKlassAdded(klassAdded);
  }
  
  @Override
  public Integer getTaxonomyAdded()
  {
    return entity.getTaxonomyAdded();
  }
  
  @Override
  public void setTaxonomyAdded(Integer taxonomyAdded)
  {
    entity.setTaxonomyAdded(taxonomyAdded);
  }
  
  @Override
  public Integer getTaxonomyRemoved()
  {
    return entity.getTaxonomyRemoved();
  }
  
  @Override
  public void setTaxonomyRemoved(Integer taxonomyRemoved)
  {
    entity.setTaxonomyRemoved(taxonomyRemoved);
  }
  
  @Override
  public Integer getNatureRelationshipChanged()
  {
    return entity.getNatureRelationshipChanged();
  }
  
  @Override
  public void setNatureRelationshipChanged(Integer natureRelationshipChanged)
  {
    entity.setNatureRelationshipChanged(natureRelationshipChanged);
  }
  
  @Override
  public Boolean getIsDefaultAssetInstanceIdChanged()
  {
    return entity.getIsDefaultAssetInstanceIdChanged();
  }
  
  @Override
  public void setIsDefaultAssetInstanceIdChanged(Boolean isDefaultAssetInstanceIdChanged)
  {
    entity.setIsDefaultAssetInstanceIdChanged(isDefaultAssetInstanceIdChanged);
  }
  
  @Override
  public Integer getMamValidityChanged()
  {
    return entity.getMamValidityChanged();
  }
  
  @Override
  public void setMamValidityChanged(Integer mamValidityChanged)
  {
    entity.setMamValidityChanged(mamValidityChanged);
  }
  
  @Override
  public Map<String, Integer> getDependentAttributeIdsCountMap()
  {
    return entity.getDependentAttributeIdsCountMap();
  }
  
  @Override
  public void setDependentAttributeIdsCountMap(
      Map<String, Integer> dependentAttributeInstanceIdsCountMap)
  {
    entity.setDependentAttributeIdsCountMap(dependentAttributeInstanceIdsCountMap);
  }

  @Override
  public void setLanguageAdded(Integer languageAdded)
  {
    entity.setLanguageAdded(languageAdded);
  }

  @Override
  public Integer getLanguageAdded()
  {
    return entity.getLanguageAdded();
  }

  @Override
  public void setLanguageRemoved(Integer languageRemoved)
  {
    entity.setLanguageRemoved(languageRemoved);
  }

  @Override
  public Integer getLanguageRemoved()
  {
    return entity.getLanguageRemoved();
  }
}
