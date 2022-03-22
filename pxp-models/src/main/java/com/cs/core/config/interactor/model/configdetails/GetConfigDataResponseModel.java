package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.config.interactor.model.datarule.GetConfigDataTagResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataResponseModel implements IGetConfigDataResponseModel {
  
  private static final long                               serialVersionUID = 1L;
  
  protected IGetConfigDataAttributeResponseModel          attributes;
  protected IGetConfigDataTagResponseModel                tags;
  protected IGetConfigDataEntityResponseModel             roles;
  protected IGetConfigDataEntityResponseModel             relationships;
  protected IGetConfigDataPropertyCollectionResponseModel propertyCollections;
  protected IGetConfigDataEntityResponseModel             attributeVariantContexts;
  protected IGetConfigDataEntityResponseModel             taxonomies;
  protected IGetConfigDataTagResponseModel                tagValues;
  protected IGetConfigDataEntityResponseModel             tabs;
  protected IGetConfigDataEntityResponseModel             natureRelationships;
  protected IGetConfigDataEntityResponseModel             contexts;
  protected IGetConfigDataEntityResponseModel             templates;
  protected IGetConfigDataEntityResponseModel             tasks;
  protected IGetConfigDataEntityResponseModel             dataRules;
  protected IGetConfigDataEntityResponseModel             systems;
  protected IGetConfigDataWorkflowResponseModel           processes;
  protected IGetConfigDataEntityResponseModel             mappings;
  protected IGetConfigDataEntityResponseModel             endpoints;
  protected IGetConfigDataEntityResponseModel             organizations;
  protected IGetConfigDataEntityResponseModel             variantContexts;
  protected IGetConfigDataEntityResponseModel             dashboards;
  protected IGetConfigDataEntityResponseModel             kpis;
  protected IGetConfigDataEntityResponseModel             authorizationMappings;
  protected IGetConfigDataUserResponseModel               users;
  protected IGetConfigDataEntityResponseModel             rootRelationships;
  
  @Override
  public IGetConfigDataAttributeResponseModel getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataAttributeResponseModel.class)
  public void setAttributes(IGetConfigDataAttributeResponseModel attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public IGetConfigDataTagResponseModel getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataTagResponseModel.class)
  public void setTags(IGetConfigDataTagResponseModel tags)
  {
    this.tags = tags;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getRoles()
  {
    return roles;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setRoles(IGetConfigDataEntityResponseModel roles)
  {
    this.roles = roles;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getRelationships()
  {
    return relationships;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setRelationships(IGetConfigDataEntityResponseModel relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getAttributeVariantContexts()
  {
    return attributeVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setAttributeVariantContexts(
      IGetConfigDataEntityResponseModel attributeVariantContexts)
  {
    this.attributeVariantContexts = attributeVariantContexts;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getTaxonomies()
  {
    return taxonomies;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setTaxonomies(IGetConfigDataEntityResponseModel taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public IGetConfigDataPropertyCollectionResponseModel getPropertyCollections()
  {
    return propertyCollections;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataPropertyCollectionResponseModel.class)
  public void setPropertyCollections(
      IGetConfigDataPropertyCollectionResponseModel propertyCollections)
  {
    this.propertyCollections = propertyCollections;
  }
  
  @Override
  public IGetConfigDataTagResponseModel getTagValues()
  {
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataTagResponseModel.class)
  public void setTagValues(IGetConfigDataTagResponseModel tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getTabs()
  {
    return tabs;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataTagResponseModel.class)
  public void setTabs(IGetConfigDataEntityResponseModel tabs)
  {
    this.tabs = tabs;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getNatureRelationships()
  {
    return natureRelationships;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataTagResponseModel.class)
  public void setNatureRelationships(IGetConfigDataEntityResponseModel natureRelationships)
  {
    this.natureRelationships = natureRelationships;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getContexts()
  {
    return contexts;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setContexts(IGetConfigDataEntityResponseModel contexts)
  {
    this.contexts = contexts;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getTasks()
  {
    return tasks;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setTasks(IGetConfigDataEntityResponseModel tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getDataRules()
  {
    return dataRules;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setDataRules(IGetConfigDataEntityResponseModel dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getSystems()
  {
    return systems;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setSystems(IGetConfigDataEntityResponseModel systems)
  {
    this.systems = systems;
  }
  
  @Override
  public IGetConfigDataWorkflowResponseModel getProcesses()
  {
    return processes;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataWorkflowResponseModel.class)
  public void setProcesses(IGetConfigDataWorkflowResponseModel processes)
  {
    this.processes = processes;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getMappings()
  {
    return mappings;
  }
  
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  @Override
  public void setMappings(IGetConfigDataEntityResponseModel mappings)
  {
    this.mappings = mappings;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getTemplates()
  {
    return templates;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setTemplates(IGetConfigDataEntityResponseModel templates)
  {
    this.templates = templates;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getEndpoints()
  {
    return endpoints;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setEndpoints(IGetConfigDataEntityResponseModel endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getVariantContexts()
  {
    return variantContexts;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setVariantContexts(IGetConfigDataEntityResponseModel variantContexts)
  {
    this.variantContexts = variantContexts;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getOrganizations()
  {
    return organizations;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setOrganizations(IGetConfigDataEntityResponseModel organizations)
  {
    this.organizations = organizations;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getDashboardTabs()
  {
    return dashboards;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setDashboardTabs(IGetConfigDataEntityResponseModel dashboards)
  {
    this.dashboards = dashboards;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getKPIs()
  {
    return kpis;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setKPIs(IGetConfigDataEntityResponseModel kpis)
  {
    this.kpis = kpis;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getAuthorizationMappings()
  {
    return authorizationMappings;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setAuthorizationMappings(IGetConfigDataEntityResponseModel authorizationMappings)
  {
    this.authorizationMappings = authorizationMappings;
  }
  
  @Override
  public IGetConfigDataUserResponseModel getUsers()
  {
    return users;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataUserResponseModel.class)
  public void setUsers(IGetConfigDataUserResponseModel users)
  {
    this.users = users;
  }
  
  @Override
  public IGetConfigDataEntityResponseModel getRootRelationships()
  {
    return rootRelationships;
  }
  
  @Override
  @JsonDeserialize(as = GetConfigDataEntityResponseModel.class)
  public void setRootRelationships(IGetConfigDataEntityResponseModel rootRelationships)
  {
    this.rootRelationships = rootRelationships;
  }
}
