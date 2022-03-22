package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.GoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveGoldenRecordRuleModel implements ISaveGoldenRecordRuleModel {
  
  private static final long           serialVersionUID = 1L;
  protected IGoldenRecordRule         entity;
  protected List<String>              addedAttributes;
  protected List<String>              deletedAttributes;
  protected List<String>              addedTags;
  protected List<String>              deletedTags;
  protected List<String>              addedKlasses;
  protected List<String>              deletedKlasses;
  protected List<String>              addedTaxonomies;
  protected List<String>              deletedTaxonomies;
  protected List<String>              addedOrganizations;
  protected List<String>              deletedOrganizations;
  protected List<String>              addedEndpoints;
  protected List<String>              deletedEndpoints;
  protected List<String>              physicalCatalogIds;
  protected IModifiedMergeEffectModel modifiedMergeEffect;
  protected List<String>              availablePhysicalCatalogs;
  
  public SaveGoldenRecordRuleModel()
  {
    entity = new GoldenRecordRule();
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
  public List<String> getAddedAttributes()
  {
    if (addedAttributes == null) {
      addedAttributes = new ArrayList<>();
    }
    return addedAttributes;
  }
  
  @Override
  public void setAddedAttributes(List<String> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<String> getDeletedAttributes()
  {
    if (deletedAttributes == null) {
      deletedAttributes = new ArrayList<>();
    }
    return deletedAttributes;
  }
  
  @Override
  public void setDeletedAttributes(List<String> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
  
  @Override
  public List<String> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new ArrayList<>();
    }
    return addedTags;
  }
  
  @Override
  public void setAddedTags(List<String> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<>();
    }
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public List<String> getAddedKlasses()
  {
    if (addedKlasses == null) {
      addedKlasses = new ArrayList<>();
    }
    return addedKlasses;
  }
  
  @Override
  public void setAddedKlasses(List<String> addedKlasses)
  {
    this.addedKlasses = addedKlasses;
  }
  
  @Override
  public List<String> getDeletedKlasses()
  {
    if (deletedKlasses == null) {
      deletedKlasses = new ArrayList<>();
    }
    return deletedKlasses;
  }
  
  @Override
  public void setDeletedKlasses(List<String> deletedKlasses)
  {
    this.deletedKlasses = deletedKlasses;
  }
  
  @Override
  public List<String> getAddedTaxonomies()
  {
    if (addedTaxonomies == null) {
      addedTaxonomies = new ArrayList<>();
    }
    return addedTaxonomies;
  }
  
  @Override
  public void setAddedTaxonomies(List<String> addedTaxonomies)
  {
    this.addedTaxonomies = addedTaxonomies;
  }
  
  @Override
  public List<String> getDeletedTaxonomies()
  {
    if (deletedTaxonomies == null) {
      deletedTaxonomies = new ArrayList<>();
    }
    return deletedTaxonomies;
  }
  
  @Override
  public void setDeletedTaxonomies(List<String> deletedTaxonomies)
  {
    this.deletedTaxonomies = deletedTaxonomies;
  }
  
  @Override
  public List<String> getAddedOrganizations()
  {
    if (addedOrganizations == null) {
      addedOrganizations = new ArrayList<>();
    }
    return addedOrganizations;
  }
  
  @Override
  public void setAddedOrganizations(List<String> addedOrganizations)
  {
    this.addedOrganizations = addedOrganizations;
  }
  
  @Override
  public List<String> getDeletedOrganizations()
  {
    if (deletedOrganizations == null) {
      deletedOrganizations = new ArrayList<>();
    }
    return deletedOrganizations;
  }
  
  @Override
  public void setDeletedOrganizations(List<String> deletedOrganizations)
  {
    this.deletedOrganizations = deletedOrganizations;
  }
  
  @Override
  public List<String> getAddedEndpoints()
  {
    if (addedEndpoints == null) {
      addedEndpoints = new ArrayList<>();
    }
    return addedEndpoints;
  }
  
  @Override
  public void setAddedEndpoints(List<String> addedEndpoints)
  {
    this.addedEndpoints = addedEndpoints;
  }
  
  @Override
  public List<String> getDeletedEndpoints()
  {
    if (deletedEndpoints == null) {
      deletedEndpoints = new ArrayList<>();
    }
    return deletedEndpoints;
  }
  
  @Override
  public void setDeletedEndpoints(List<String> deletedEndpoints)
  {
    this.deletedEndpoints = deletedEndpoints;
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
  public IModifiedMergeEffectModel getModifiedMergeEffect()
  {
    return modifiedMergeEffect;
  }
  
  @JsonDeserialize(as = ModifiedMergeEffectModel.class)
  @Override
  public void setModifiedMergeEffect(IModifiedMergeEffectModel modifiedMergeEffect)
  {
    this.modifiedMergeEffect = modifiedMergeEffect;
  }
  
  /**
   * ********************* IGNORED_PROPPERTIES
   * ******************************************
   */
  @JsonIgnore
  @Override
  public List<String> getAttributes()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAttributes(List<String> attributes)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getTags()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTags(List<String> tags)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getKlassIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getTaxonomyIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getOrganizations()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setOrganizations(List<String> organizations)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getEndpoints()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public IMergeEffect getMergeEffect()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setMergeEffect(IMergeEffect mergeEffect)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setType(String type)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getCode()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setCode(String code)
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
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
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
  
  @Override
  public List<String> getAvailablePhysicalCatalogs()
  {
    return this.availablePhysicalCatalogs;
  }
  
  @Override
  public void setAvailablePhysicalCatalogs(List<String> availablePhysicalCatalogs)
  {
    this.availablePhysicalCatalogs = availablePhysicalCatalogs;
  }
}
