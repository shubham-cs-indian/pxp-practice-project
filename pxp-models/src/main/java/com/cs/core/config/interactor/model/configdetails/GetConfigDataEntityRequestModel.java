package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.config.interactor.model.datarule.GetConfigDataTagValuesPaginationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataEntityRequestModel implements IGetConfigDataEntityRequestModel {
  
  private static final long                                 serialVersionUID = 1L;
  
  protected IGetConfigDataEntityPaginationModel             attributes;
  protected IGetConfigDataEntityPaginationModel             tags;
  protected IGetConfigDataEntityPaginationModel             roles;
  protected IGetConfigDataEntityPaginationModel             relationships;
  protected IGetConfigDataEntityPaginationModel             attributeVariantContexts;
  protected IGetConfigDataEntityPaginationModel             taxonomies;
  protected IGetConfigDataPropertyCollectionPaginationModel propertyCollections;
  protected IGetConfigDataTagValuesPaginationModel          tagValues;
  protected IGetConfigDataTagValuesPaginationModel          contextTagValues;
  protected IGetConfigDataEntityPaginationModel             tabs;
  protected IGetConfigDataEntityPaginationModel             natureRelationships;
  protected IGetConfigDataEntityPaginationModel             contexts;
  protected IGetConfigDataEntityPaginationModel             templates;
  protected IGetConfigDataEntityPaginationModel             tasks;
  protected IGetConfigDataEntityPaginationModel             dataRules;
  protected IGetConfigDataEntityPaginationModel             systems;
  protected IGetConfigDataWorkflowPaginationModel           processes;
  protected IGetConfigDataEntityPaginationModel             mappings;
  protected IGetConfigDataEndpointPaginationModel           endpoints;
  protected IGetConfigDataEntityPaginationModel             variantContexts;
  protected IGetConfigDataEntityPaginationModel             organizations;
  protected IGetConfigDataEntityPaginationModel             dashboardTabs;
  protected IGetConfigDataEntityPaginationModel             kpis;
  protected IGetConfigDataEntityPaginationModel             references;
  protected IGetConfigDataEntityPaginationModel             natureReferences;
  protected IGetConfigDataEntityPaginationModel             authorizationMappings;
  protected IGetConfigDataEntityPaginationModel             users;
  protected IGetConfigDataEntityPaginationModel             rootRelationships;
  
  @Override
  public IGetConfigDataEntityPaginationModel getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setAttributes(IGetConfigDataEntityPaginationModel attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setTags(IGetConfigDataEntityPaginationModel tags)
  {
    this.tags = tags;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getRoles()
  {
    return roles;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setRoles(IGetConfigDataEntityPaginationModel roles)
  {
    this.roles = roles;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getRelationships()
  {
    return relationships;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setRelationships(IGetConfigDataEntityPaginationModel relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getAttributeVariantContexts()
  {
    return attributeVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setAttributeVariantContexts(
      IGetConfigDataEntityPaginationModel attributeVariantContexts)
  {
    this.attributeVariantContexts = attributeVariantContexts;
  }
  
  @Override
  public IGetConfigDataPropertyCollectionPaginationModel getPropertyCollections()
  {
    return propertyCollections;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataPropertyCollectionPaginationModel.class)
  public void setPropertyCollections(
      IGetConfigDataPropertyCollectionPaginationModel propertyCollections)
  {
    this.propertyCollections = propertyCollections;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getTaxonomies()
  {
    return taxonomies;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setTaxonomies(IGetConfigDataEntityPaginationModel taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public IGetConfigDataTagValuesPaginationModel getTagValues()
  {
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataTagValuesPaginationModel.class)
  public void setTagValues(IGetConfigDataTagValuesPaginationModel tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getTabs()
  {
    return tabs;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setTabs(IGetConfigDataEntityPaginationModel tabs)
  {
    this.tabs = tabs;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setNatureRelationships(IGetConfigDataEntityPaginationModel natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getContexts()
  {
    return contexts;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setContexts(IGetConfigDataEntityPaginationModel contexts)
  {
    this.contexts = contexts;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getTemplates()
  {
    return templates;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setTemplates(IGetConfigDataEntityPaginationModel templates)
  {
    this.templates = templates;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getTasks()
  {
    return tasks;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setTasks(IGetConfigDataEntityPaginationModel tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getDataRules()
  {
    return dataRules;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setDataRules(IGetConfigDataEntityPaginationModel dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getSystems()
  {
    return systems;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setSystems(IGetConfigDataEntityPaginationModel systems)
  {
    this.systems = systems;
  }
  
  @Override
  public IGetConfigDataWorkflowPaginationModel getProcesses()
  {
    return processes;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataWorkflowPaginationModel.class)
  public void setProcesses(IGetConfigDataWorkflowPaginationModel processes)
  {
    this.processes = processes;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getMappings()
  {
    return mappings;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setMappings(IGetConfigDataEntityPaginationModel mappings)
  {
    this.mappings = mappings;
  }
  
  @Override
  public IGetConfigDataEndpointPaginationModel getEndpoints()
  {
    return endpoints;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEndpointPaginationModel.class)
  public void setEndpoints(IGetConfigDataEndpointPaginationModel endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getVariantContexts()
  {
    return variantContexts;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setVariantContexts(IGetConfigDataEntityPaginationModel variantContexts)
  {
    this.variantContexts = variantContexts;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getOrganizations()
  {
    return organizations;
  }
  
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  @Override
  public void setOrganizations(IGetConfigDataEntityPaginationModel organizations)
  {
    this.organizations = organizations;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getDashboardTabs()
  {
    return dashboardTabs;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setDashboardTabs(IGetConfigDataEntityPaginationModel dashboardTabs)
  {
    this.dashboardTabs = dashboardTabs;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getKPIs()
  {
    return kpis;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setKPIs(IGetConfigDataEntityPaginationModel kpis)
  {
    this.kpis = kpis;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getReferences()
  {
    return references;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setReferences(IGetConfigDataEntityPaginationModel references)
  {
    this.references = references;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getNatureReferences()
  {
    return natureReferences;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setNatureReferences(IGetConfigDataEntityPaginationModel natureReferences)
  {
    this.natureReferences = natureReferences;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getAuthorizationMappings()
  {
    return authorizationMappings;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setAuthorizationMappings(IGetConfigDataEntityPaginationModel authorizationMappings)
  {
    this.authorizationMappings = authorizationMappings;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getUsers()
  {
    return users;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setUsers(IGetConfigDataEntityPaginationModel users)
  {
    this.users = users;
  }
  
  @Override
  public IGetConfigDataEntityPaginationModel getRootRelationships()
  {
    return rootRelationships;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityPaginationModel.class)
  public void setRootRelationships(IGetConfigDataEntityPaginationModel rootRelationships)
  {
    this.rootRelationships = rootRelationships;
  }
  
  @Override
  public IGetConfigDataTagValuesPaginationModel getContextTagValues()
  {
    return contextTagValues;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataTagValuesPaginationModel.class)
  public void setContextTagValues(IGetConfigDataTagValuesPaginationModel contextTagValues)
  {
    this.contextTagValues = contextTagValues;
  }
}
