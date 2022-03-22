package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataResponseModel extends IModel {
  
  public static final String ATTRIBUTES                 = "attributes";
  public static final String TAGS                       = "tags";
  public static final String ROLES                      = "roles";
  public static final String RELATIONSHIPS              = "relationships";
  public static final String PROPERTY_COLLECTIONS       = "propertyCollections";
  public static final String ATTRIBUTE_VARIANT_CONTEXTS = "attributeVariantContexts";
  public static final String TAXONOMIES                 = "taxonomies";
  public static final String TAG_VALUES                 = "tagValues";
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
  public static final String AUTHORIZATION_MAPPING      = "authorizationMappings";
  public static final String USERS                      = "users";
  public static final String ROOT_RELATIONSHIPS         = "rootRelationships";
  
  public IGetConfigDataAttributeResponseModel getAttributes();
  
  public void setAttributes(IGetConfigDataAttributeResponseModel attributes);
  
  public IGetConfigDataTagResponseModel getTags();
  
  public void setTags(IGetConfigDataTagResponseModel tags);
  
  public IGetConfigDataEntityResponseModel getRoles();
  
  public void setRoles(IGetConfigDataEntityResponseModel roles);
  
  public IGetConfigDataEntityResponseModel getRelationships();
  
  public void setRelationships(IGetConfigDataEntityResponseModel relationships);
  
  public IGetConfigDataPropertyCollectionResponseModel getPropertyCollections();
  
  public void setPropertyCollections(
      IGetConfigDataPropertyCollectionResponseModel propertyCollections);
  
  public IGetConfigDataEntityResponseModel getAttributeVariantContexts();
  
  public void setAttributeVariantContexts(
      IGetConfigDataEntityResponseModel attributeVariantContexts);
  
  public IGetConfigDataEntityResponseModel getTaxonomies();
  
  public void setTaxonomies(IGetConfigDataEntityResponseModel taxonomies);
  
  public IGetConfigDataTagResponseModel getTagValues();
  
  public void setTagValues(IGetConfigDataTagResponseModel tagValues);
  
  public IGetConfigDataEntityResponseModel getTabs();
  
  public void setTabs(IGetConfigDataEntityResponseModel tabs);
  
  public IGetConfigDataEntityResponseModel getNatureRelationships();
  
  public void setNatureRelationships(IGetConfigDataEntityResponseModel natureRelationships);
  
  public IGetConfigDataEntityResponseModel getContexts();
  
  public void setContexts(IGetConfigDataEntityResponseModel contexts);
  
  public IGetConfigDataEntityResponseModel getTemplates();
  
  public void setTemplates(IGetConfigDataEntityResponseModel templates);
  
  public IGetConfigDataEntityResponseModel getTasks();
  
  public void setTasks(IGetConfigDataEntityResponseModel tasks);
  
  public IGetConfigDataEntityResponseModel getDataRules();
  
  public void setDataRules(IGetConfigDataEntityResponseModel dataRules);
  
  public IGetConfigDataEntityResponseModel getSystems();
  
  public void setSystems(IGetConfigDataEntityResponseModel systems);
  
  public IGetConfigDataWorkflowResponseModel getProcesses();
  
  public void setProcesses(IGetConfigDataWorkflowResponseModel processes);
  
  public IGetConfigDataEntityResponseModel getMappings();
  
  public void setMappings(IGetConfigDataEntityResponseModel mappings);
  
  public IGetConfigDataEntityResponseModel getEndpoints();
  
  public void setEndpoints(IGetConfigDataEntityResponseModel endpoints);
  
  public IGetConfigDataEntityResponseModel getVariantContexts();
  
  public void setVariantContexts(IGetConfigDataEntityResponseModel variantContexts);
  
  public IGetConfigDataEntityResponseModel getOrganizations();
  
  public void setOrganizations(IGetConfigDataEntityResponseModel organizations);
  
  public IGetConfigDataEntityResponseModel getDashboardTabs();
  
  public void setDashboardTabs(IGetConfigDataEntityResponseModel dashboards);
  
  public IGetConfigDataEntityResponseModel getKPIs();
  
  public void setKPIs(IGetConfigDataEntityResponseModel kpis);
  
  public IGetConfigDataEntityResponseModel getAuthorizationMappings();
  
  public void setAuthorizationMappings(IGetConfigDataEntityResponseModel authorizationMappings);
  
  public IGetConfigDataUserResponseModel getUsers();
  
  public void setUsers(IGetConfigDataUserResponseModel users);
  
  public IGetConfigDataEntityResponseModel getRootRelationships();
  
  public void setRootRelationships(IGetConfigDataEntityResponseModel rootRelationships);
}
