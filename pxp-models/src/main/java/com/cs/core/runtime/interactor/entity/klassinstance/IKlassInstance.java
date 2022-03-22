package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IKlassInstance extends IRuntimeEntity {
  
  public static final String NAME                  = "name";
  
  public static final String BASETYPE              = "baseType";
  public static final String TAXONOMY_IDS          = "taxonomyIds";
  public static final String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static final String TYPES                 = "types";
  public static final String COMPONENT_ID          = "componentId";
  public static final String TAGS                  = "tags";
  public static final String ATTRIBUTES            = "attributes";
  public static final String ROLES                 = "roles";
  public static final String ORGANIZATION_ID       = "organizationId";
  public static final String ENDPOINT_ID           = "endpointId";
  public static final String PHYSICAL_CATALOG_ID   = "physicalCatalogId";
  public static final String PORTAL_ID             = "portalId";
  public static final String LOGICAL_CATALOG_ID    = "logicalCatalogId";
  public static final String SYSTEM_ID             = "systemId";
  public static final String ORIGINAL_INSTANCE_ID  = "originalInstanceId";
  
  public String getName();
  
  public void setName(String name);
  
  public List<? extends IContentTagInstance> getTags();
  
  public void setTags(List<? extends IContentTagInstance> tags);
  
  public List<? extends IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<? extends IContentAttributeInstance> attributeInstances);
  
  public List<? extends IRoleInstance> getRoles();
  
  public void setRoles(List<? extends IRoleInstance> roles);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public List<String> getSelectedTaxonomyIds();
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
}
