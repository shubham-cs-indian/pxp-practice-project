package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataEntityRequestModel extends IModel {
  
  public static final String ATTRIBUTES                 = "attributes";
  public static final String TAGS                       = "tags";
  public static final String ROLES                      = "roles";
  public static final String RELATIONSHIPS              = "relationships";
  public static final String PROPERTY_COLLECTIONS       = "propertyCollections";
  public static final String ATTRIBUTE_VARIANT_CONTEXTS = "attributeVariantContexts";
  public static final String TAXONOMIES                 = "taxonomies";
  public static final String TAG_VALUES                 = "tagValues";
  public static final String CONTEXT_TAG_VALUES         = "contextTagValues";
  public static final String TABS                       = "tabs";
  public static final String NATURE_RELATIONSHIPS       = "natureRelationships";
  public static final String CONTEXTS                   = "contexts";
  public static final String TEMPLATES                  = "templates";
  public static final String TASKS                      = "tasks";
  public static final String DATARULES                  = "dataRules";
  public static final String PROCESSES                  = "processes";
  public static final String SYSTEMS                    = "systems";
  public static final String MAPPINGS                   = "mappings";
  public static final String ENDPOINTS                  = "endpoints";
  public static final String VARIANT_CONTEXTS           = "variantContexts";
  public static final String ORGANIZATIONS              = "organizations";
  public static final String DASHBOARD_TABS             = "dashboardTabs";
  public static final String KPIS                       = "kpis";
  public static final String REFERENCES                 = "references";
  public static final String NATURE_REFERENCES          = "natureReferences";
  public static final String AUTHORIZATION_MAPPING      = "authorizationMappings";
  public static final String USERS                      = "users";
  public static final String ROOT_RELAIONSHIPS          = "rootRelationships";
  
  public IGetConfigDataEntityPaginationModel getAttributes();
  
  public void setAttributes(IGetConfigDataEntityPaginationModel attributes);
  
  public IGetConfigDataEntityPaginationModel getTags();
  
  public void setTags(IGetConfigDataEntityPaginationModel tags);
  
  public IGetConfigDataEntityPaginationModel getRoles();
  
  public void setRoles(IGetConfigDataEntityPaginationModel roles);
  
  public IGetConfigDataEntityPaginationModel getRelationships();
  
  public void setRelationships(IGetConfigDataEntityPaginationModel relationships);
  
  public IGetConfigDataPropertyCollectionPaginationModel getPropertyCollections();
  
  public void setPropertyCollections(
      IGetConfigDataPropertyCollectionPaginationModel propertyCollections);
  
  public IGetConfigDataEntityPaginationModel getAttributeVariantContexts();
  
  public void setAttributeVariantContexts(
      IGetConfigDataEntityPaginationModel attributeVariantContexts);
  
  public IGetConfigDataEntityPaginationModel getTaxonomies();
  
  public void setTaxonomies(IGetConfigDataEntityPaginationModel taxonomies);
  
  public IGetConfigDataTagValuesPaginationModel getTagValues();
  
  public void setTagValues(IGetConfigDataTagValuesPaginationModel tagValues);
  
  public IGetConfigDataTagValuesPaginationModel getContextTagValues();
  
  public void setContextTagValues(IGetConfigDataTagValuesPaginationModel contextTagValues);
  
  public IGetConfigDataEntityPaginationModel getTabs();
  
  public void setTabs(IGetConfigDataEntityPaginationModel tabs);
  
  public IGetConfigDataEntityPaginationModel getNatureRelationships();
  
  public void setNatureRelationships(IGetConfigDataEntityPaginationModel natureRelationships);
  
  public IGetConfigDataEntityPaginationModel getContexts();
  
  public void setContexts(IGetConfigDataEntityPaginationModel contexts);
  
  public IGetConfigDataEntityPaginationModel getTemplates();
  
  public void setTemplates(IGetConfigDataEntityPaginationModel templates);
  
  public IGetConfigDataEntityPaginationModel getTasks();
  
  public void setTasks(IGetConfigDataEntityPaginationModel tasks);
  
  public IGetConfigDataEntityPaginationModel getDataRules();
  
  public void setDataRules(IGetConfigDataEntityPaginationModel dataRules);
  
  public IGetConfigDataEntityPaginationModel getSystems();
  
  public void setSystems(IGetConfigDataEntityPaginationModel systems);
  
  public IGetConfigDataWorkflowPaginationModel getProcesses();
  
  public void setProcesses(IGetConfigDataWorkflowPaginationModel processes);
  
  public IGetConfigDataEntityPaginationModel getMappings();
  
  public void setMappings(IGetConfigDataEntityPaginationModel mappings);
  
  public IGetConfigDataEndpointPaginationModel getEndpoints();
  
  public void setEndpoints(IGetConfigDataEndpointPaginationModel endpoints);
  
  public IGetConfigDataEntityPaginationModel getVariantContexts();
  
  public void setVariantContexts(IGetConfigDataEntityPaginationModel variantContexts);
  
  public IGetConfigDataEntityPaginationModel getOrganizations();
  
  public void setOrganizations(IGetConfigDataEntityPaginationModel organizations);
  
  public IGetConfigDataEntityPaginationModel getDashboardTabs();
  
  public void setDashboardTabs(IGetConfigDataEntityPaginationModel dashboardTabs);
  
  public IGetConfigDataEntityPaginationModel getKPIs();
  
  public void setKPIs(IGetConfigDataEntityPaginationModel kpis);
  
  public IGetConfigDataEntityPaginationModel getReferences();
  
  public void setReferences(IGetConfigDataEntityPaginationModel references);
  
  public IGetConfigDataEntityPaginationModel getNatureReferences();
  
  public void setNatureReferences(IGetConfigDataEntityPaginationModel natureReferences);
  
  public IGetConfigDataEntityPaginationModel getAuthorizationMappings();
  
  public void setAuthorizationMappings(IGetConfigDataEntityPaginationModel authorizationMappings);
  
  public IGetConfigDataEntityPaginationModel getUsers();
  
  public void setUsers(IGetConfigDataEntityPaginationModel users);
  
  public IGetConfigDataEntityPaginationModel getRootRelationships();
  
  public void setRootRelationships(IGetConfigDataEntityPaginationModel rootRelationships);
}
