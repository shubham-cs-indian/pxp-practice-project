package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IMulticlassificationRequestModel extends IModel {
  
  public static final String KLASS_IDS                           = "klassIds";
  public static final String IS_UNLINKED_RELATIONSHIPS           = "isUnlinkedRelationships";
  public static final String TAXONOMY_IDS_FOR_DETAILS            = "taxonomyIdsForDetails";
  public static final String TEMPLATE_ID                         = "templateId";
  public static final String COLLECTION_IDS                      = "collectionIds";
  public static final String TAG_ID_TAG_VALUE_IDS_MAP            = "tagIdTagValueIdsMap";
  public static final String SHOULD_USE_TAG_ID_TAG_VALUE_IDS_MAP = "shouldUseTagIdTagValueIdsMap";
  public static final String TAB_ID                              = "tabId";
  public static final String TYPE_ID                             = "typeId";
  public static final String ORAGANIZATION_ID                    = "organizationId";
  public static final String ENDPOINT_ID                         = "endpointId";
  public static final String PHYSICAL_CATALOG_ID                 = "physicalCatalogId";
  public static final String PORTAL_ID                           = "portalId";
  public static final String USER_ID                             = "userId";
  public static final String CONTEXT_ID                          = "contextId";
  public static final String IS_GRID_EDITABLE                    = "isGridEditable";
  // For Variants Parent Klass And Taxonomy Details
  public static final String PARENT_KLASS_IDS                    = "parentKlassIds";
  public static final String PARENT_TAXONOMY_IDS                 = "parentTaxonomyIds";
  public static final String SHOULD_SEND_TAXONOMY_HIERARCHIES    = "shouldSendTaxonomyHierarchies";
  public static final String PROCESS_INSTANCE_ID                 = "processInstanceId";
  public static final String LANGUAGE_CODES                      = "languageCodes";
  public static final String SELECTED_TAXONOMY_IDS               = "selectedTaxonomyIds";
  public static final String BASE_TYPE                           = "baseType";
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public Boolean getIsUnlinkedRelationships();
  
  public void setIsUnlinkedRelationships(Boolean isUnlinkedRelationships);
  
  public List<String> getTaxonomyIdsForDetails();
  
  /**
   * Get Taxonomy hierarchies for the respective taxonomy ids
   *
   * @param taxonomyIdsForDetails
   */
  public void setTaxonomyIdsForDetails(List<String> taxonomyIdsForDetails);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public List<String> getCollectionIds();
  
  public void setCollectionIds(List<String> collectionIds);
  
  public Map<String, List<String>> getTagIdTagValueIdsMap();
  
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap);
  
  public Boolean getShouldUseTagIdTagValueIdsMap();
  
  public void setShouldUseTagIdTagValueIdsMap(Boolean shouldUseTagIdTagValueIdsMap);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public Boolean getIsGridEditable();
  
  public void setIsGridEditable(Boolean isGridEditable);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public List<String> getParentKlassIds();
  
  public void setParentKlassIds(List<String> parentKlassIds);
  
  public List<String> getParentTaxonomyIds();
  
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds);
  
  public Boolean getShouldSendTaxonomyHierarchies();
  
  public void setShouldSendTaxonomyHierarchies(Boolean shouldSendTaxonomyHierarchies);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
