package com.cs.core.config.interactor.entity.goldenrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GoldenRecordRule implements IGoldenRecordRule {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          code;
  protected String          icon;
  protected String          iconKey;
  protected List<String>    attributes;
  protected List<String>    tags;
  protected List<String>    klassIds;
  protected List<String>    taxonomyIds;
  protected List<String>    physicalCatalogIds;
  protected List<String>    organizations;
  protected List<String>    endpoints;
  protected IMergeEffect    mergeEffect;
  protected Boolean         isAutoCreate;
  
  @Override
  public List<String> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<String> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<String> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<String> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    if (physicalCatalogIds == null) {
      physicalCatalogIds = new ArrayList<>();
    }
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public List<String> getOrganizations()
  {
    if (organizations == null) {
      organizations = new ArrayList<>();
    }
    return organizations;
  }
  
  @Override
  public void setOrganizations(List<String> organizations)
  {
    this.organizations = organizations;
  }
  
  @Override
  public List<String> getEndpoints()
  {
    if (endpoints == null) {
      endpoints = new ArrayList<>();
    }
    return endpoints;
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setType(String type)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
  }
  
  @Override
  public IMergeEffect getMergeEffect()
  {
    return mergeEffect;
  }
  
  @JsonDeserialize(as = MergeEffect.class)
  @Override
  public void setMergeEffect(IMergeEffect mergeEffect)
  {
    this.mergeEffect = mergeEffect;
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    if (isAutoCreate == null) {
      isAutoCreate = false;
    }
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
}
