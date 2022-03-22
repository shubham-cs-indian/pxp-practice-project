package com.cs.core.config.interactor.entity.goldenrecord;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;

import java.util.List;

public interface IGoldenRecordRule extends IConfigMasterEntity {
  
  public static final String ATTRIBUTES           = "attributes";
  public static final String TAGS                 = "tags";
  public static final String KLASS_IDS            = "klassIds";
  public static final String TAXONOMY_IDS         = "taxonomyIds";
  public static final String PHYSICAL_CATALOG_IDS = "physicalCatalogIds";
  public static final String ORGANIZATIONS        = "organizations";
  public static final String ENDPOINTS            = "endpoints";
  public static final String MERGE_EFFECT         = "mergeEffect";
  public static final String IS_AUTO_CREATE       = "isAutoCreate";
  
  public List<String> getAttributes();
  
  public void setAttributes(List<String> attributes);
  
  public List<String> getTags();
  
  public void setTags(List<String> tags);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getOrganizations();
  
  public void setOrganizations(List<String> organizations);
  
  public List<String> getEndpoints();
  
  public void setEndpoints(List<String> endpoints);
  
  public IMergeEffect getMergeEffect();
  
  public void setMergeEffect(IMergeEffect mergeEffect);
  
  public Boolean getIsAutoCreate();
  
  public void setIsAutoCreate(Boolean isAutoCreate);
}
