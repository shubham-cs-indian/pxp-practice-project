package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveGoldenRecordRuleModel extends IGoldenRecordRule, IModel {
  
  public static final String ID                          = "id";
  public static final String LABEL                       = "label";
  public static final String ICON                        = "icon";
  public static final String ADDED_ATTRIBUTES            = "addedAttributes";
  public static final String DELETED_ATTRIBUTES          = "deletedAttributes";
  public static final String ADDED_TAGS                  = "addedTags";
  public static final String DELETED_TAGS                = "deletedTags";
  public static final String ADDED_KLASSES               = "addedKlasses";
  public static final String DELETED_KLASSES             = "deletedKlasses";
  public static final String ADDED_TAXONOMIES            = "addedTaxonomies";
  public static final String DELETED_TAXONOMIES          = "deletedTaxonomies";
  public static final String ADDED_ORGANIZATIONS         = "addedOrganizations";
  public static final String DELETED_ORGANIZATIONS       = "deletedOrganizations";
  public static final String ADDED_ENDPOINTS             = "addedEndpoints";
  public static final String DELETED_ENDPOINTS           = "deletedEndpoints";
  public static final String PHYSICAL_CATALOG_IDS        = "physicalCatalogIds";
  public static final String MODIFIED_MERGE_EFFECT       = "modifiedMergeEffect";
  public static final String AVAILABLE_PHYSICAl_CATALOGS = "availablePhysicalCatalogs";
  
  public List<String> getAddedAttributes();
  
  public void setAddedAttributes(List<String> deletedAttributes);
  
  public List<String> getDeletedAttributes();
  
  public void setDeletedAttributes(List<String> deletedAttributes);
  
  public List<String> getAddedTags();
  
  public void setAddedTags(List<String> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<String> getAddedKlasses();
  
  public void setAddedKlasses(List<String> addedKlasses);
  
  public List<String> getDeletedKlasses();
  
  public void setDeletedKlasses(List<String> deletedKlasses);
  
  public List<String> getAddedTaxonomies();
  
  public void setAddedTaxonomies(List<String> addedTaxonomies);
  
  public List<String> getDeletedTaxonomies();
  
  public void setDeletedTaxonomies(List<String> deletedTaxonomies);
  
  public List<String> getAddedOrganizations();
  
  public void setAddedOrganizations(List<String> addedOrganizations);
  
  public List<String> getDeletedOrganizations();
  
  public void setDeletedOrganizations(List<String> deletedOrganizations);
  
  public List<String> getAddedEndpoints();
  
  public void setAddedEndpoints(List<String> addedEndpoints);
  
  public List<String> getDeletedEndpoints();
  
  public void setDeletedEndpoints(List<String> deletedEndpoints);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public IModifiedMergeEffectModel getModifiedMergeEffect();
  
  public void setModifiedMergeEffect(IModifiedMergeEffectModel modifiedMergeEffect);
  
  public List<String> getAvailablePhysicalCatalogs();
  
  public void setAvailablePhysicalCatalogs(List<String> availablePhysicalCatalogs);
}
