package com.cs.core.runtime.interactor.model.typeswitch;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MulticlassificationRequestModel implements IMulticlassificationRequestModel {
  
  private static final long           serialVersionUID              = 1L;
  protected List<String>              klassIds                      = new ArrayList<>();
  protected List<String>              selectedTaxonomyIds           = new ArrayList<>();
  protected Boolean                   isUnlinkedRelationships       = false;
  protected List<String>              taxonomyIdsForDetails         = new ArrayList<>();
  protected String                    templateId;
  protected List<String>              collectionIds;
  protected Map<String, List<String>> tagIdTagValueIdsMap           = new HashMap<>();
  protected Boolean                   shouldUseTagIdTagValueIdsMap  = true;
  protected String                    tabId;
  protected String                    typeId;
  protected String                    endpointId;
  protected String                    organizationId;
  protected String                    physicalCatalogId;
  protected String                    portalId;
  protected String                    userId;
  protected String                    contextId;
  protected List<String>              parentKlassIds;
  protected List<String>              parentTaxonomyIds;
  protected Boolean                   shouldSendTaxonomyHierarchies = false;
  protected String                    processInstanceId;
  protected List<String>              languageCodes;
  protected String                    baseType;
  protected Boolean                   isGridEditable                = false;
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    if (klassIds == null) {
      klassIds = new ArrayList<>();
    }
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> taxonomyIds)
  {
    if (selectedTaxonomyIds == null) {
      selectedTaxonomyIds = new ArrayList<>();
    }
    this.selectedTaxonomyIds = taxonomyIds;
  }
  
  @Override
  public Boolean getIsUnlinkedRelationships()
  {
    return isUnlinkedRelationships;
  }
  
  @Override
  public void setIsUnlinkedRelationships(Boolean isUnlinkedRelationships)
  {
    this.isUnlinkedRelationships = isUnlinkedRelationships;
  }
  
  @Override
  public List<String> getTaxonomyIdsForDetails()
  {
    return taxonomyIdsForDetails;
  }
  
  @Override
  public void setTaxonomyIdsForDetails(List<String> taxonomyIdsForDetails)
  {
    if (taxonomyIdsForDetails == null) {
      taxonomyIdsForDetails = new ArrayList<>();
    }
    this.taxonomyIdsForDetails = taxonomyIdsForDetails;
  }
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
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
    this.parentTaxonomyIds = parentTaxonomyIds;
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
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
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
