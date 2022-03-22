package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.GoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.MergeEffect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GoldenRecordRuleModel implements IGoldenRecordRuleModel {
  
  private static final long   serialVersionUID = 1L;
  protected IGoldenRecordRule entity;
  
  public GoldenRecordRuleModel()
  {
    entity = new GoldenRecordRule();
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return entity.getKlassIds();
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    entity.setKlassIds(klassIds);
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return entity.getTaxonomyIds();
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    entity.setTaxonomyIds(taxonomyIds);
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    return entity.getPhysicalCatalogIds();
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    entity.setPhysicalCatalogIds(physicalCatalogIds);
  }
  
  @Override
  public List<String> getOrganizations()
  {
    return entity.getOrganizations();
  }
  
  @Override
  public void setOrganizations(List<String> organizations)
  {
    entity.setOrganizations(organizations);
  }
  
  @Override
  public List<String> getEndpoints()
  {
    return entity.getEndpoints();
  }
  
  @Override
  public void setEndpoints(List<String> endPoints)
  {
    entity.setEndpoints(endPoints);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public String getType()
  {
    return null;
  }
  
  @Override
  public void setType(String type)
  {
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  public void setAttributes(List<String> attributes)
  {
    entity.setAttributes(attributes);
  }
  
  @Override
  public List<String> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  public void setTags(List<String> tags)
  {
    entity.setTags(tags);
  }
  
  @Override
  public IMergeEffect getMergeEffect()
  {
    return entity.getMergeEffect();
  }
  
  @Override
  @JsonDeserialize(as = MergeEffect.class)
  public void setMergeEffect(IMergeEffect mergeEffect)
  {
    entity.setMergeEffect(mergeEffect);
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    return entity.getIsAutoCreate();
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    entity.setIsAutoCreate(isAutoCreate);
  }
}
