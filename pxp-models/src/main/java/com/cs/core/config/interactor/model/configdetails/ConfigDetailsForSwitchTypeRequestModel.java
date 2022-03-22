package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForImportSwitchTypeRequestModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConfigDetailsForSwitchTypeRequestModel
    implements IConfigDetailsForImportSwitchTypeRequestModel {
  
  private static final long         serialVersionUID              = 1L;
  protected String                  processInstanceId;
  protected List<String>            languageCodes;
  protected List<String>            addedKlassIds;
  protected List<String>            addedTaxonomyIds;
  protected List<String>            deletedTaxonomyIds;
  protected List<String>            taxonomyIds;
  protected Boolean                 isGridEditable                = false;
  private List<String>              klassIds;
  private List<String>              taxonomyIdsForDetails;
  private String                    componentId;
  private List<String>              collectionIds                 = new ArrayList<>();
  private String                    deletedTaxonomyId;
  private List<String>              selectedTaxonomyIds;
  private Map<String, List<String>> tagIdTagValueIdsMap           = new HashMap<>();
  private Boolean                   shouldUseTagIdTagValueIdsMap  = true;
  private String                    tabId;
  private String                    typeId;
  private String                    endpointId;
  private String                    organizationId;
  private String                    physicalCatalogId;
  private String                    portalId;
  private String                    userId;
  private String                    contextId;
  private List<String>              parentKlassIds;
  private List<String>              parentTaxonomyIds;
  private Boolean                   shouldSendTaxonomyHierarchies = false;
  private Boolean                   isUnlinkedRelationship;
  
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
  public List<String> getTaxonomyIdsForDetails()
  {
    if (taxonomyIdsForDetails == null) {
      taxonomyIdsForDetails = new ArrayList<>();
    }
    return taxonomyIdsForDetails;
  }
  
  @Override
  public void setTaxonomyIdsForDetails(List<String> taxonomyIdsForDetails)
  {
    this.taxonomyIdsForDetails = taxonomyIdsForDetails;
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsUnlinkedRelationships()
  {
    return isUnlinkedRelationship;
  }
  
  @Override
  @JsonIgnore
  public void setIsUnlinkedRelationships(Boolean isUnlinkedRelationships)
  {
    this.isUnlinkedRelationship = isUnlinkedRelationships;
  }
  
  @Override
  @JsonIgnore
  public String getTemplateId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setTemplateId(String templateId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getCollectionIds()
  {
    if (collectionIds == null) {
      collectionIds = new ArrayList<>();
    }
    return collectionIds;
  }
  
  @Override
  public void setCollectionIds(List<String> collectionIds)
  {
    this.collectionIds = collectionIds;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
  
  @Override
  public String getDeletedTaxonomyId()
  {
    return deletedTaxonomyId;
  }
  
  @Override
  public void setDeletedTaxonomyId(String deletedTaxonomyId)
  {
    this.deletedTaxonomyId = deletedTaxonomyId;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<String>();
    }
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public Map<String, List<String>> getTagIdTagValueIdsMap()
  {
    return tagIdTagValueIdsMap;
  }
  
  @Override
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap)
  {
    this.tagIdTagValueIdsMap = tagIdTagValueIdsMap;
  }
  
  @Override
  public Boolean getShouldUseTagIdTagValueIdsMap()
  {
    return shouldUseTagIdTagValueIdsMap;
  }
  
  @Override
  public void setShouldUseTagIdTagValueIdsMap(Boolean shouldUseTagIdTagValueIdsMap)
  {
    this.shouldUseTagIdTagValueIdsMap = shouldUseTagIdTagValueIdsMap;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public String getTypeId()
  {
    return typeId;
  }
  
  @Override
  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public List<String> getParentKlassIds()
  {
    if (parentKlassIds == null) {
      parentKlassIds = new ArrayList<>();
    }
    return parentKlassIds;
  }
  
  @Override
  public void setParentKlassIds(List<String> parentKlassIds)
  {
    this.parentKlassIds = parentKlassIds;
  }
  
  @Override
  public List<String> getParentTaxonomyIds()
  {
    if (parentTaxonomyIds == null) {
      parentTaxonomyIds = new ArrayList<>();
    }
    return parentTaxonomyIds;
  }
  
  @Override
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds)
  {
    this.parentKlassIds = parentTaxonomyIds;
  }
  
  @Override
  public Boolean getShouldSendTaxonomyHierarchies()
  {
    return shouldSendTaxonomyHierarchies;
  }
  
  @Override
  public void setShouldSendTaxonomyHierarchies(Boolean shouldSendTaxonomyHierarchies)
  {
    this.shouldSendTaxonomyHierarchies = shouldSendTaxonomyHierarchies;
  }
  
  @Override
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    if (addedTaxonomyIds == null) {
      addedTaxonomyIds = new ArrayList<>();
    }
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    if (deletedTaxonomyIds == null) {
      deletedTaxonomyIds = new ArrayList<>();
    }
    
    if (deletedTaxonomyId != null) {
      deletedTaxonomyIds.add(deletedTaxonomyId);
    }
    
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  @Override
  public List<String> getAddedKlassIds()
  {
    if (addedKlassIds == null) {
      addedKlassIds = new ArrayList<>();
    }
    
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
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
  
  @JsonIgnore
  @Override
  public String getBaseType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setBaseType(String baseType)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsGridEditable()
  {
    return isGridEditable;
  }
  
  @Override
  public void setIsGridEditable(Boolean isGridEditable)
  {
    this.isGridEditable = isGridEditable;
  }
}
